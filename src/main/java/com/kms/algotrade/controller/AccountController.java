package com.kms.algotrade.controller;

import com.kms.algotrade.entity.Account;
import com.kms.algotrade.security.AuthenticationDto;
import com.kms.algotrade.security.JwtAuthProvider;
import com.kms.algotrade.security.LoginDto;
import com.kms.algotrade.security.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final JwtAuthProvider jwtAuthProvider;

    private final LoginService loginService;

    @PostMapping("/login2")
    public void login(@RequestBody Account account){
        System.out.println("Account :: " + account.getAccountId());
        System.out.println("Account :: " + account.getPassword());
    }

    @PostMapping(value = {"/signin"})
    public ResponseEntity<AuthenticationDto> appLogin(
            @RequestBody LoginDto loginDto) throws Exception {

        AuthenticationDto authentication = loginService.loginService(loginDto);

        return ResponseEntity.ok()
                .header("accesstoken", jwtAuthProvider
                        .createToken(
                                authentication.getId(),
                                authentication.getEmail())
                        ).body(authentication);
    }
}
