package com.kms.algotrade.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    String accountId;

    String email;

    String password;

    String passwordConfirm;

    @Override
    public String toString() {
        return "RegisterDto{" +
                "accountId='" + accountId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                '}';
    }
}
