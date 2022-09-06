package com.caubogeo.bogeo.jwt;

import com.caubogeo.bogeo.domain.auth.Authority;
import com.caubogeo.bogeo.dto.jwt.TokenDto;
import com.caubogeo.bogeo.exceptionhandler.AuthorityExceptionType;
import com.caubogeo.bogeo.exceptionhandler.BizException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class TokenProvider {
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;

    public TokenProvider() {
        this.ACCESS_TOKEN_EXPIRE_TIME = JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
        this.REFRESH_TOKEN_EXPIRE_TIME = JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;
        byte[] keyBytes = Decoders.BASE64.decode(JwtProperties.SECRET);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    protected String createToken(String email, Set<Authority> auth, long tokenValid) {
        // ex) sub : abc@abc.com
        Claims claims = Jwts.claims().setSubject(email);

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

    public Authentication getAuthentication(String accessToken) throws BizException {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get(JwtProperties.AUTHORIZATION_HEADER) == null || !StringUtils.hasText(claims.get(JwtProperties.AUTHORIZATION_HEADER).toString())) {
            throw new BizException(AuthorityExceptionType.NOT_FOUND_AUTHORITY);  // 유저에게 권한 없음
        }

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

    public int validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return 1;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return 2;
        } catch (Exception e) {
            log.info("잘못된 토큰입니다.");
            return -1;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {  // 만료된 토큰이어도 파싱한다
            return e.getClaims();
        }
    }
}
