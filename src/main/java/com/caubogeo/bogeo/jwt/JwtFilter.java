package com.caubogeo.bogeo.jwt;

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
import java.io.PrintWriter;

import static com.caubogeo.bogeo.jwt.JwtProperties.AUTHORIZATION_HEADER;
import static com.caubogeo.bogeo.jwt.JwtProperties.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
        } else {
            String token = resolveToken(request);

            log.debug("token = {}", token);

            if(StringUtils.hasText(token)) {
                int flag = tokenProvider.validateToken(token);
                // 유효한 토큰
                if(flag == 1) {
                    this.setAuthentication(token);
                } else if(flag == 2) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    log.debug("doFilterInternal Exception CALL!");
                    out.println("{\"error\": \"ACCESS_TOKEN_EXPIRED\", \"message\" : \"액세스 토큰이 만료되었습니다.\"}");
                } else {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    log.debug("doFilterInternal Exception CALL!");
                    out.println("{\"error\": \"BAD_TOKEN\", \"message\" : \"잘못된 토큰 값입니다.\"}");
                }
            }
            else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.println("{\"error\": \"EMPTY_TOKEN\", \"message\" : \"토큰 값이 비어있습니다.\"}");
            }
        }
    }

    private void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
