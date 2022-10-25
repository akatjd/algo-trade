package com.kms.algotrade.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/trade")
public class TradeController {

    @GetMapping(value = "/main")
    public String getMainPageData() {
        System.out.println("들어옴");
        return "testReturn";
    }
}
