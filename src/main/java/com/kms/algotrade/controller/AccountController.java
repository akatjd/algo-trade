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

    @Autowired
    AccountServiceImpl accountService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterDto registerDto) {
        accountService.register(registerDto);
    }
}
