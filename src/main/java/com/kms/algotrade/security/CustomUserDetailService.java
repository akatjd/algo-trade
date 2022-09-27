package com.kms.algotrade.security;

import com.kms.algotrade.entity.Account;
import com.kms.algotrade.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private AccountRepository accountRepository;

    @Autowired
    public CustomUserDetailService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        //여기서 받은 유저 패스워드와 비교하여 로그인 인증

        System.out.println("accountId :: " + accountId);

        Optional<Account> account = accountRepository.findByAccountId(accountId);

        System.out.println(account.toString());

//        if (account.get() == null){
//            throw new UsernameNotFoundException("User not authorized.");
//        }
        return new SecurityAccount(account.get());
    }
}
