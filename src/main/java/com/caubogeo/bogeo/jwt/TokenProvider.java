package com.caubogeo.bogeo.jwt;

import com.caubogeo.bogeo.domain.auth.Authority;
import com.caubogeo.bogeo.dto.jwt.TokenDto;
import com.caubogeo.bogeo.exceptionhandler.AuthException;
import com.caubogeo.bogeo.exceptionhandler.AuthorityExceptionType;
import com.caubogeo.bogeo.exceptionhandler.JwtException;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Transactional
@RequiredArgsConstructor
@Component
public class TokenProvider {
    private final long ACCESS_TOKEN_EXPIRE_TIME = JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME = JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtProperties.SECRET));
    private final RefreshTokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    protected String createToken(String id, Set<Authority> auth, long tokenValid) {
        // ex) sub : abc@abc.com
        Claims claims = Jwts.claims().setSubject(id);

        // ex)  auth : ROLE_USER,ROLE_ADMIN
        claims.put(JwtProperties.AUTHORIZATION_HEADER, auth.stream()  // doubt
                .map(Authority::getAuthorityName)
                .collect(Collectors.joining(",")));

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValid))
                .signWith(SignatureAlgorithm.HS256, key)   // DOUBT
                .compact();
    }

    public String createAccessToken(String id, Set<Authority> auth){
        return this.createToken(id, auth, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(String id, Set<Authority> auth) {
        return this.createToken(id, auth, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String getMemberIdByToken(String token) {
        return this.parseClaims(token).getSubject();
    }

    public TokenDto createTokenDto(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(JwtProperties.BEARER_PREFIX)
                .build();
    }

    public Authentication getAuthentication(String token) throws JwtException {
        // 토큰 복호화
        Claims claims = parseClaims(token);

        if(claims.get(JwtProperties.AUTHORIZATION_HEADER) == null || !StringUtils.hasText(claims.get(JwtProperties.AUTHORIZATION_HEADER).toString())) {
            log.debug("not found authority = {}", claims.get(JwtProperties.AUTHORIZATION_HEADER).toString());
            throw new AuthException(AuthorityExceptionType.NOT_FOUND_AUTHORITY);  // 유저에게 권한 없음
        }

        // debug log
        log.debug("claims.getAuth = {}", claims.get(JwtProperties.AUTHORIZATION_HEADER));
        log.debug("claims.getId = {}", claims.getSubject());

        // 클레임에서 권한 정보 가져옴
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtProperties.AUTHORIZATION_HEADER).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        authorities.stream().forEach(o->{
            log.debug("getAuthentication -> authorities = {}", o.getAuthority());
        });

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new CustomIdPasswordAuthToken(principal, "", authorities);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {  // 만료된 토큰이어도 파싱한다
            return e.getClaims();
        }
    }
    // Request의 Header에서 AccessToken 값을 가져옵니다. "Authentication" : "token"
    public String resolveAccessToken(HttpServletRequest request) {
        if(request.getHeader(JwtProperties.AUTHORIZATION_HEADER) != null )
            return request.getHeader(JwtProperties.AUTHORIZATION_HEADER).substring(7);
        return null;
    }
    // Request의 Header에서 RefreshToken 값을 가져옵니다. "refreshToken" : "token"
    public String resolveRefreshToken(HttpServletRequest request) {
        if(request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
            log.debug("expiration: {}", claims.getBody().getExpiration());
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.debug("expired exception!!");
            log.debug(e.getMessage());
            return false;
        }
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(JwtProperties.AUTHORIZATION_HEADER, accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", refreshToken);
    }

    public boolean existsRefreshToken(String refreshToken) {
        return tokenRepository.existsByValue(refreshToken);
    }

    public Set<Authority> getRoles(String id) {
        return memberRepository.findById(id).get().getAuthorities();
    }
}
