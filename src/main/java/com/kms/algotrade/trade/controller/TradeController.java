package com.kms.algotrade.trade.controller;

import com.kms.algotrade.trade.dto.StartTradeDto;
import com.kms.algotrade.trade.service.TradeService;
import com.kms.algotrade.trade.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@Controller
@Slf4j
@RequestMapping("/api/trade")
public class TradeController {
    private final TradeService tradeService;

    private final WebSocketService webSocketService;

    private final SimpMessagingTemplate webSocket;

    public TradeController(TradeService tradeService,
                           SimpMessagingTemplate webSocket,
                           WebSocketService webSocketService) {
        this.tradeService = tradeService;
        this.webSocket = webSocket;
        this.webSocketService = webSocketService;
    }

    @GetMapping(value = "/main")
    public Map<String, Object> getMainPageData() {
        return tradeService.getMainPageData();
    }

    @PostMapping(value = "/startTrade")
    public void startTrade(@RequestBody StartTradeDto startTradeDto) {
        System.out.println(startTradeDto);
    }

    @GetMapping(value = "/getUpbitAccountInfo")
    public void getUpbitAccountInfo() {
        tradeService.getUpbitAccountInfo();
    }

    @GetMapping(value = "/conUpbitWebsocket")
    public ArrayList<String> conUpbitWebsocket(@RequestParam String url) {
        // 비트 티커만 커넥
        ArrayList<String> bitTicker = new ArrayList<>();
//        bitTicker.add("KRW-BTC");
//        bitTicker.add("KRW-XRP");
        bitTicker.add("KRW-SAND");
        webSocketService.conUpbitWebsocket(url, bitTicker);
        return bitTicker;
    }
}
