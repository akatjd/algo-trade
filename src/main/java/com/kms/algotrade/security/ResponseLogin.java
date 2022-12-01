package com.kms.algotrade.security;

import lombok.*;

@Getter
@Setter
@ToString
public class ResponseLogin {
    private String accessToken;
    private String refreshToken;
    private String tradeStatus;

    @Builder
    public ResponseLogin(String accessToken, String refreshToken, String tradeStatus) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tradeStatus = tradeStatus;
    }
}
