package com.kms.algotrade.controller;

import com.kms.algotrade.dto.RegisterDto;
import com.kms.algotrade.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AccountController {
    public static final String SECURED_TEXT = "Hello from the secured resource!";

    @Autowired
    AccountServiceImpl accountService;

    @GetMapping("/login")
    public void login() {
        // 시큐리티 로그인이 성공하면 해당 api 메서드로 들어옴.
        log.info("Post successfully called on /login resource");
    }
    @PostMapping("/register")
    public void register(@RequestBody RegisterDto registerDto) {
        accountService.register(registerDto);
    }

    // 테스트용
    @GetMapping("/hello")
    public void hello() {
        System.out.println("HelloWorld");
    }
}
