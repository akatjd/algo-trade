package com.kms.algotrade.security;

import com.kms.algotrade.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String email;

    private String password;


    public Account toEntity() {

        Account build = Account.builder()
                .email(email)
                .password(password)
                .build();

        return build;
    }
}
