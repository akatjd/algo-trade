package com.kms.algotrade.account.controller;

import com.kms.algotrade.account.dto.RegisterDto;
import com.kms.algotrade.account.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class AccountController {

    private final AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterDto registerDto) {
        accountService.register(registerDto);
    }
}
