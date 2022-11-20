package com.kms.algotrade.security.controller;

import com.kms.algotrade.account.dto.LoginDto;
import com.kms.algotrade.security.service.AuthService;
import com.kms.algotrade.security.config.JwtFilter;
import com.kms.algotrade.security.ResponseLogin;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseLogin> authorize(@RequestBody LoginDto loginDto) {
        ResponseLogin responseLogin = authService.authenticate(loginDto.getUsername(), loginDto.getPassword());

        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, responseLogin.getAccessToken());
        httpHeaders.add(JwtFilter.REFRESH_HEADER, responseLogin.getRefreshToken());

        return new ResponseEntity<>(responseLogin, httpHeaders, HttpStatus.OK);
    }
}
