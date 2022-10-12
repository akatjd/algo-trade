package com.kms.algotrade.security;

import com.kms.algotrade.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
//    private final TokenProvider tokenProvider;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    private final AccountRepository accountRepository;
//
//    public AuthService(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, AccountRepository accountRepository) {
//        this.tokenProvider = tokenProvider;
//        this.authenticationManagerBuilder = authenticationManagerBuilder;
//        this.accountRepository = accountRepository;
//    }

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountRepository accountRepository;
    // username 과 패스워드로 사용자를 인증하여 액세스토큰을 반환한다.
    public ResponseLogin authenticate(String username, String password) {
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        System.out.println(authenticationToken);
        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 그 객체를 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.generateToken(authentication);

        return ResponseLogin.builder()
                .accessToken(accessToken)
                .build();
    }
}
