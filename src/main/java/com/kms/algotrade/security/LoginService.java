package com.kms.algotrade.security;

import com.kms.algotrade.entity.Account;
import com.kms.algotrade.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationDto loginService(LoginDto loginDto) {

        // dto -> entity
        Account loginEntity = loginDto.toEntity();

        // 회원 엔티티 객체 생성 및 조회시작
        Optional<Account> member = accountRepository.findByAccountId(loginEntity.getAccountId());

        if (!bCryptPasswordEncoder.matches(loginEntity.getPassword(), member.get().getPassword()))
            throw new ForbiddenException("Passwords do not match");

        ModelMapper modelMapper = new ModelMapper();

        // 회원정보를 인증클래스 객체(authentication)로 매핑
//        AuthenticationDto authentication = modelMapper.toDto(member, AuthenticationDto.class);
        AuthenticationDto authentication = modelMapper.map(member, AuthenticationDto.class);

        return authentication;
    }
}
