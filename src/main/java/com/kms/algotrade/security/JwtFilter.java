package com.kms.algotrade.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    private TokenProvider jwtTokenProvider;

    public JwtFilter(TokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        System.out.println("token :: " + token);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                System.out.println("auth :: " + auth);
                // 정상 토큰이면 토큰을 통해 생성한 Authentication 객체를 SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(500, "filter쪽 에러요");
            return;
        }

        filterChain.doFilter(request, response); // 다음 필터 체인 실행
    }
}
