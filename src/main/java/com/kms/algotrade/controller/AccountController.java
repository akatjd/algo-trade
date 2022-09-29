package com.kms.algotrade.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AccountController {
    public static final String SECURED_TEXT = "Hello from the secured resource!";
    @GetMapping("/login")
    public void login() {
        log.info("GET successfully called on /login resource");
    }
    @PostMapping("/register")
    public void register(@RequestBody Map<String, Object> userDto) {
        System.out.println("register 들어옴");
    }

    // 테스트용
    @GetMapping("/hello")
    public void hello() {
        System.out.println("HelloWorld");
    }
}
