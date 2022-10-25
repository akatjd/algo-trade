package com.kms.algotrade.security;

import lombok.*;

@Getter
@Setter
@ToString
public class ResponseLogin {
    private String accessToken;
    private String refreshToken;

    @Builder
    public ResponseLogin(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
