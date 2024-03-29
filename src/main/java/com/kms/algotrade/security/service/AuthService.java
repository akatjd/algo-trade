package com.kms.algotrade.security.service;

import com.kms.algotrade.account.entity.Account;
import com.kms.algotrade.account.repository.AccountRepository;
import com.kms.algotrade.security.ResponseLogin;
import com.kms.algotrade.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final AccountRepository accountRepository;

    public AuthService(AccountRepository accountRepository,
                       AuthenticationManager authenticationManager,
                       TokenProvider tokenProvider) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    // username 과 패스워드로 사용자를 인증하여 액세스토큰을 반환한다.
    public ResponseLogin authenticate(String accountId, String password) {
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountId, password);
        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 그 객체를 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken();

        // refresh token 저장
        Optional<Account> account = accountRepository.findByAccountId(accountId);
        account.ifPresent(r -> {
                    r.setAccessToken(accessToken);
                    r.setRefreshToken(refreshToken);
                    accountRepository.save(r);
                });

        return ResponseLogin.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tradeStatus(account.get().getTradeStatus())
                .build();
    }
}
