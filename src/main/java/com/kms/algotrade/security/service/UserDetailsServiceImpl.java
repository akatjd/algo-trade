package com.kms.algotrade.security.service;

import com.kms.algotrade.account.entity.Account;
import com.kms.algotrade.account.repository.AccountRepository;
import com.kms.algotrade.security.SecurityAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        log.info("accountId :: {}", accountId);

        //여기서 받은 유저 패스워드와 비교하여 로그인 인증
        Optional<Account> account = accountRepository.findByAccountId(accountId);
        System.out.println(account.toString());
        if (account.get() == null){
            throw new UsernameNotFoundException("User not authorized.");
        }

        SecurityAccount securityAccount = new SecurityAccount(account.get());
        System.out.println(securityAccount.getAuthorities());

        return securityAccount;
    }
}
