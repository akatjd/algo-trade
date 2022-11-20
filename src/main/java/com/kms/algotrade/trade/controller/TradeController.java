package com.kms.algotrade.trade.controller;

import com.kms.algotrade.trade.dto.StartTradeDto;
import com.kms.algotrade.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/trade")
public class TradeController {
    @Autowired
    TradeService tradeService;

    @GetMapping(value = "/main")
    public Map<String, Object> getMainPageData() {
        return tradeService.getMainPageData();
    }

    @PostMapping(value = "/startTrade")
    public void startTrade(@RequestBody StartTradeDto startTradeDto) {
        System.out.println(startTradeDto);
    }
}
