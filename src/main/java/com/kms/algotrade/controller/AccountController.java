package com.kms.algotrade.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    @PostMapping("/login")
    public void login(@RequestBody Map<String, Object> reqMap){
        System.out.println("reqMap");
        System.out.println("들어옵니까!?!?!?!?");
    }
}
