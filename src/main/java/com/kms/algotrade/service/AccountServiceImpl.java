package com.kms.algotrade.service;

import com.kms.algotrade.dto.RegisterDto;
import com.kms.algotrade.entity.Account;
import com.kms.algotrade.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService{

    @Autowired
    AccountRepository accountRepository;

    @Transactional
    @Override
    public void register(RegisterDto registerDto) {
        checkRegisterDataValues(registerDto);
        Account savedAccount = saveAccount(registerDto);
        log.info("savedAccount :: {}", savedAccount.toString());
    }

    public void checkRegisterDataValues(RegisterDto registerDto){
        Optional<Account> accountOpt = Optional.ofNullable(accountRepository.findByAccountId(registerDto.getAccountId())
                .orElse(null));

        if(!accountOpt.isEmpty()){
            throw new RuntimeException("이미 가입된 아이디가 있습니다.");
        } else if (!(registerDto.getPassword()).equals(registerDto.getPasswordConfirm())) {
            throw new RuntimeException("비밀번호가 같지 않습니다.");
        }
    }

    public Account saveAccount(RegisterDto registerDto){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Account account = Account.builder()
                .accountId(registerDto.getAccountId())
                .password(bCryptPasswordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .role("ROLE_USER")
                .build();

        accountRepository.save(account);
        return account;
    }
}
