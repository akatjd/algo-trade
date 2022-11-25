package com.kms.algotrade.trade.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.algotrade.enums.MarketPriceType;
import com.kms.algotrade.trade.dto.CalRsiPropertiesDto;
import com.kms.algotrade.trade.entity.UpbitProperties;
import com.kms.algotrade.trade.listener.UpbitWebsocketListener;
import com.kms.algotrade.trade.repository.UpbitPropertiesRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final UpbitPropertiesRepository upbitPropertiesRepository;

    private final UpbitWebsocketListener webSocketListener;

    public WebSocketServiceImpl(UpbitPropertiesRepository upbitPropertiesRepository,
                                UpbitWebsocketListener webSocketListener) {
        this.upbitPropertiesRepository = upbitPropertiesRepository;
        this.webSocketListener = webSocketListener;
    }

    @Override
    public void conUpbitWebsocket(String url, List<String> tickerList) {

        //컨트롤러에서 계정정보도 넘어왔다고 가정
        Integer accountSeq = 1;
        Optional<UpbitProperties> upOpt = upbitPropertiesRepository.findById(accountSeq);
        UpbitProperties upbitProperties = upOpt.orElseThrow();

        // KMS 2022-08-04 RSI 가져와서 websocket쪽으로 넘기기
        CalRsiPropertiesDto rsiProperties = getUpbitBtcKrwCalRsiProperties();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .pingInterval(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(url).build();

        log.info("tickerList :: " + tickerList);

        webSocketListener.setParameter(MarketPriceType.TRADE, tickerList, rsiProperties, upbitProperties);

        client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public CalRsiPropertiesDto getUpbitBtcKrwCalRsiProperties() {

        OkHttpClient client = new OkHttpClient();

//        String market = "KRW-BTC";
//        String market = "KRW-XRP"; // 2017-10-22~24일 데이터가 없음
        String market = "KRW-SAND";

        // 최대 카운트 설정 (1~200)
        int count = 200;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String url = "https://api.upbit.com/v1/candles/days?count=" + count + "&market=" + market;
        StringBuilder urlSb = new StringBuilder(url);

        Map<String, Double> resultMap = new HashMap<>();
        String lastDay = null;
        List<Map<String, Object>> resBodyMapList;
        Request request;
        Response response;
        int loopIdx = 0;
        do {
            resBodyMapList = new ArrayList<>();
            urlSb.setLength(0);
            urlSb.append(url + "&to=" + now.minusDays(count*loopIdx).format(dateTimeFormatter));

            request = new Request.Builder().url(urlSb.toString()).addHeader("market", market).get()
                    .addHeader("Accept", "application/json").build();

            try {
                response = client.newCall(request).execute();
                resBodyMapList = new ObjectMapper().readValue(response.body().string(),
                        new TypeReference<List<Map<String, Object>>>() {
                        });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            for (Map<String, Object> resBodyMap : resBodyMapList) {
                lastDay = (String) resBodyMap.get("candle_date_time_kst");
                log.info("(String)resBodyMap.get(\"candle_date_time_kst\") :: {}",
                        (String) resBodyMap.get("candle_date_time_kst"));
                resultMap.put((String) resBodyMap.get("candle_date_time_kst"), (Double) resBodyMap.get("trade_price"));
            }

//            cal.add(Calendar.DATE, -count);
            loopIdx++;

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } while (resBodyMapList.size() > 0);

        log.info("size :: {}", resultMap.size());
        log.info("resultMap :: {}", resultMap);
        log.info("lastDay :: {}", lastDay);

        // 여기부터 RSI 구하기
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime ldtLastDay = LocalDateTime.parse(lastDay, dateTimeFormatter);

        log.info("cal get Time :: {}", ldtLastDay);

        double au = 0.0;
        double ad = 0.0;
        double lastPrice = 0.0;

        for (int i = 0; i < resultMap.size()-2; i++) {

            String tdy = ldtLastDay.plusDays(i).format(dateTimeFormatter);
            String tmr = ldtLastDay.plusDays(i+1).format(dateTimeFormatter);


            double tdyPrice = resultMap.get(tdy);
            double tmrPrice = resultMap.get(tmr);
            double diffPrice = tmrPrice - tdyPrice;
//            log.info("tmr :: {}", tmr);
//            log.info("tdyPrice :: {}", tdyPrice);
//            log.info("tmrPrice :: {}", tmrPrice);

            if (i < 13) {
                if (diffPrice > 0) {
                    au += diffPrice;
                } else {
                    ad += (-diffPrice);
                }
            } else if (i == 13) {
                if (diffPrice > 0) {
                    au = (au + diffPrice) / 14;
                } else {
                    ad = (ad - diffPrice) / 14;
                }
            } else {
                if (diffPrice > 0) {
                    au = (au * 13 + diffPrice) / 14;
                    ad = (ad * 13) / 14;
                } else {
                    au = (au * 13) / 14;
                    ad = (ad * 13 - diffPrice) / 14;
                }
            }

            lastPrice = tmrPrice;
            if (resultMap.size() - 3 == i) {
                lastDay = tdy;
            }
        }

        double rsi = 100 * au / (au + ad);

        System.out.println("au :: " + au);
        System.out.println("ad :: " + ad);
        System.out.println("rsi :: " + rsi);
        System.out.println("lastPrice :: " + lastPrice);

        CalRsiPropertiesDto rsiProperties = new CalRsiPropertiesDto();

        rsiProperties.setAvgUp(au);
        rsiProperties.setAvgDown(ad);
        rsiProperties.setLastPrice(lastPrice);
        rsiProperties.setLastDay(lastDay);

        return rsiProperties;
    }
}
