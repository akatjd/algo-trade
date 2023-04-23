package com.kms.algotrade.trade.controller;

import com.kms.algotrade.trade.dto.StartTradeDto;
import com.kms.algotrade.trade.service.TradeService;
import com.kms.algotrade.trade.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        String url = "wss://api.upbit.com/websocket/v1";
        // 비트 티커만 커넥
        ArrayList<String> bitTicker = new ArrayList<>();
//        bitTicker.add("KRW-BTC");
//        bitTicker.add("KRW-XRP");
        bitTicker.add("KRW-SAND");
        webSocketService.conUpbitWebsocket(url, bitTicker);
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

    @GetMapping(value = "/testCon")
    public void testCon() {
        // 비트 티커만 커넥
        ArrayList<String> bitTicker = new ArrayList<>();


        try {
            URL url = new URL("https://api.upbit.com/v1/market/all");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            System.out.println("result :: ");
            System.out.println(result);

            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String market = obj.getString("market");
                if (market.startsWith("KRW")) {
                    System.out.println(market);
                    bitTicker.add(market);
                }
            }

            String wsUrl = "wss://api.upbit.com/websocket/v1";
            webSocketService.conUpbitWebsocket(wsUrl, bitTicker);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }



    }
}
