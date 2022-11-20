package com.kms.algotrade.security;

import com.kms.algotrade.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class SecurityAccount extends User {
    private Account account;

    public SecurityAccount(Account account) {
        super(account.getAccountId(), account.getPassword(), makeGrantedAuthority(account));
        this.account = account;
    }

    public Account getAccount() {
        return this.account;
    }

    private static List<GrantedAuthority> makeGrantedAuthority(Account account){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(account.getRole()));
        return list;
    }
}
