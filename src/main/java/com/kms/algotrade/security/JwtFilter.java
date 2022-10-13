package com.kms.algotrade.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private TokenProvider jwtTokenProvider;

    public JwtFilter(TokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request, AUTHORIZATION_HEADER);
        System.out.println("token :: " + token);
        try {
            if (token != null && jwtTokenProvider.validateToken(token) == TokenProvider.JwtCode.ACCESS) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                System.out.println("auth :: " + auth);
                // 정상 토큰이면 토큰을 통해 생성한 Authentication 객체를 SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else if (token != null && jwtTokenProvider.validateToken(token) == TokenProvider.JwtCode.EXPIRED) {
                String refresh = jwtTokenProvider.resolveToken(request, REFRESH_HEADER);
                System.out.println("refresh :: " + refresh);
                // refresh token을 확인해서 재발급해준다
                if(refresh != null && jwtTokenProvider.validateToken(refresh) == TokenProvider.JwtCode.ACCESS){
                    log.info("들어와여1111");
                    String newRefresh = jwtTokenProvider.reissueRefreshToken(refresh);
                    if(newRefresh != null){
//                        response.setHeader(REFRESH_HEADER, "Bearer-"+newRefresh);
                        response.setHeader(REFRESH_HEADER, newRefresh);

                        // access token 생성
                        Authentication authentication = jwtTokenProvider.getAuthentication(refresh);
//                        response.setHeader(AUTHORIZATION_HEADER, "Bearer-"+jwtTokenProvider.generateToken(authentication));
                        response.setHeader(AUTHORIZATION_HEADER, jwtTokenProvider.generateAccessToken(authentication));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("reissue refresh Token & access Token");
                    }else {
                        response.sendError(500, "doFilterInternal error");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(500, "doFilterInternal error");
            return;
        }

        filterChain.doFilter(request, response); // 다음 필터 체인 실행
    }
}
