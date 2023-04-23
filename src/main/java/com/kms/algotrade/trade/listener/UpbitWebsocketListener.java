package com.kms.algotrade.trade.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.kms.algotrade.enums.MarketPriceType;
import com.kms.algotrade.trade.dto.CalRsiPropertiesDto;
import com.kms.algotrade.trade.entity.UpbitProperties;
import com.kms.algotrade.util.JsonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class UpbitWebsocketListener extends WebSocketListener {

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private String json;
    private double au;
    private double ad;
    private double lastPrice;
    private Date tdy;
    int cnt = 1;

    private OkHttpClient client;

    private String accessKey;
    private String secretKey;
    private String serverUrl;

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("Socket Closed :: %s / %s\r\n", code, reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("Socket Closing :: %s / %s\n", code, reason);
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        webSocket.cancel();
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        webSocket.cancel();
        log.info("Socket Error :: " + t.getMessage());
        log.info("Socket :: " + webSocket);

        client = new OkHttpClient();
        Request request = webSocket.request();
        webSocket = client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        JsonNode jsonNode = JsonUtil.fromJson(text, JsonNode.class);
        log.info("onMessage string :: " + jsonNode.toPrettyString());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {

        log.info("tdy :: {}", tdy);

        JsonNode jsonNode = JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), JsonNode.class);

        log.info("jsonNode :: " + jsonNode.toPrettyString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tradeDateStr = jsonNode.findValue("trade_date").asText();
        try {
            Date tradeDate = sdf.parse(tradeDateStr);
            log.info("tradeDate :: {}", tradeDate);

            if(tradeDate.after(tdy)) {
                double newLastPrice = jsonNode.findValue("prev_closing_price").asDouble();
                double tempDiffPrice = newLastPrice - lastPrice;
                if(tempDiffPrice > 0) {
                    this.au = (au * 13  + tempDiffPrice) / 14;
                    this.ad = (ad * 13) / 14;
                }else {
                    this.au = (au * 13) / 14;
                    this.ad = (ad * 13 - tempDiffPrice) / 14;
                }
                tdy = tradeDate;
                lastPrice = newLastPrice;
                log.info("au :: {}", au);
                log.info("ad :: {}", ad);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        double tradePrice = jsonNode.findValue("trade_price").asDouble();
        log.info("trade_price :: " + jsonNode);
        double diffPrice = tradePrice - lastPrice;

        double nowAu;
        double nowAd;

        if(diffPrice > 0) {
            nowAu = (au * 13  + diffPrice) / 14;
            nowAd = (ad * 13) / 14;
        }else {
            nowAu = (au * 13) / 14;
            nowAd = (ad * 13 - diffPrice) / 14;
        }

        double rsi = 100*nowAu/(nowAu+nowAd);

        log.info("rsi :: " + rsi);

//        if(rsi < 30) { // 매수
////        if(cnt == 2) {
//            String market = "KRW-SAND";
//            String side = "bid";
//            String volume = "";
//            String price = "1"; // 일단 5000원 테스트
//            String ordType = "price"; // 시장가 매수
//
//            String ticker = market.split("-")[1];
//
//            List<UpbitAccountDto> accountInfoDtoList = upbitTradeService.getAccountInfo();
//
//            for(UpbitAccountDto dto : accountInfoDtoList) {
//                String currency = dto.getCurrency();
//                Double balance = dto.getBalance();
//                if(ticker.equals(currency) && balance<0.01) {
//                    log.info("currency :: {}", currency);
//                    log.info("balance :: {}", balance);
//                    try {
////                        upbitTradeService.postOrders(market, side, volume, price, ordType);
//                    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }else if(rsi > 60) { // 매도
//            String market = "KRW-SAND";
//            String side = "bid";
//            String volume = "";
//            String price = "1"; // 일단 5000원 테스트
//            String ordType = "price"; // 시장가 매수
//
//            String ticker = market.split("-")[1];
//
//            List<UpbitAccountDto> accountInfoDtoList = upbitTradeService.getAccountInfo();
//
//            for(UpbitAccountDto dto : accountInfoDtoList) {
//                String currency = dto.getCurrency();
//                Double balance = dto.getBalance();
//                if(ticker.equals(currency) && balance>0.1) {
//                    try {
//                        upbitTradeService.postOrders(market, side, volume, price, ordType);
//                    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        webSocket.send(getParameter());
//        webSocket.close(NORMAL_CLOSURE_STATUS, null); // 없을 경우 끊임없이 서버와 통신함
    }

    public void setParameter(MarketPriceType marketPriceType, List<String> codes, CalRsiPropertiesDto rsiProperties, UpbitProperties upbitProperties) {
        List<Object> jsonMapList = new ArrayList<>();
        Ticket ticket = Ticket.builder().ticket(UUID.randomUUID().toString()).build();
        Type type = Type.builder().codes(codes).type(marketPriceType).build();
        jsonMapList.add(ticket);
        jsonMapList.add(type);
        this.json = JsonUtil.toJson(jsonMapList);

        this.au = rsiProperties.getAvgUp();
        this.ad = rsiProperties.getAvgDown();
        this.lastPrice = rsiProperties.getLastPrice();

        DateTimeFormatter datTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String tdyStr = datTimeFormatter.format(LocalDateTime.now());

        this.tdy = java.sql.Date.valueOf(tdyStr);

        this.accessKey = upbitProperties.getAccessKey();
        this.secretKey = upbitProperties.getSecretKey();
        this.serverUrl = upbitProperties.getServerUrl();
    }

    private String getParameter() {
        return this.json;
    }

    @Getter
    private static class Ticket {
        @Builder
        public Ticket(String ticket) {
            this.ticket = ticket;
            // TODO Auto-generated constructor stub
        }

        private String ticket = "";
    }

    @Getter
    private static class Type {
        @Builder
        public Type(MarketPriceType type, List<String> codes) {
            this.type = type;
            this.codes = codes;
            // TODO Auto-generated constructor stub
        }
        private MarketPriceType type = null;
        private List<String> codes = null;
    }
}
