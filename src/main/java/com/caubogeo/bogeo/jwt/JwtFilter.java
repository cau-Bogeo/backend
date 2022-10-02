package com.caubogeo.bogeo.jwt;

import com.caubogeo.bogeo.domain.auth.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static com.caubogeo.bogeo.jwt.JwtProperties.AUTHORIZATION_HEADER;
import static com.caubogeo.bogeo.jwt.JwtProperties.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT Filter...");
        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = tokenProvider.resolveAccessToken(request);
        String refreshToken = tokenProvider.resolveRefreshToken(request);

        // 유효한 토큰인지 확인합니다.
        if (accessToken != null) {
            // 어세스 토큰이 유효한 상황
            if (tokenProvider.validateToken(accessToken)) {
                this.setAuthentication(accessToken);
            }
            // 어세스 토큰이 만료된 상황 | 리프레시 토큰 또한 존재하는 상황
            else if (!tokenProvider.validateToken(accessToken) && refreshToken != null) {
                log.debug("refresh is not expired!");
                log.debug("refreshToken : {}", refreshToken);
                // 재발급 후, 컨텍스트에 다시 넣기
                /// 리프레시 토큰 검증
                boolean validateRefreshToken = tokenProvider.validateToken(refreshToken);
                /// 리프레시 토큰 저장소 존재유무 확인
                boolean isRefreshToken = tokenProvider.existsRefreshToken(refreshToken);
                log.debug("isRefreshToken : {}", isRefreshToken);
                if (validateRefreshToken && isRefreshToken) {
                    /// 리프레시 토큰으로 이메일 정보 가져오기
                    String email = tokenProvider.getMemberIdByToken(refreshToken);
                    log.debug("email : {}", email);

                    /// 이메일로 권한정보 받아오기
                    Set<Authority> roles = tokenProvider.getRoles(email);
                    log.debug("roles : {}", roles.size());

                    /// 토큰 발급
                    String newAccessToken = tokenProvider.createAccessToken(email, roles);
                    log.debug("new access token : {}", newAccessToken);

                    /// 헤더에 어세스 토큰 추가
                    tokenProvider.setHeaderAccessToken(response, newAccessToken);

                    /// 컨텍스트에 넣기
                    this.setAuthentication(newAccessToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
