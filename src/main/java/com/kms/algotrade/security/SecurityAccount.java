package com.kms.algotrade.security;

import java.util.ArrayList;
import java.util.List;

public class SecurityAccount extends User {

    private static final long serialVersionUID = 1L;

    public SecurityAccount(Account account) {
        super(account.getAccountId(), account.getPassword(), makeGrantedAuthority(account));
    }

    private static List<GrantedAuthority> makeGrantedAuthority(Account account){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(account.getRole()));
        return list;
    }
}
