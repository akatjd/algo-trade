package com.kms.algotrade.trade.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.algotrade.enums.MarketPriceType;
import com.kms.algotrade.trade.dto.CalRsiPropertiesDto;
import com.kms.algotrade.trade.dto.CryptoExchangeInfoDto;
import com.kms.algotrade.trade.dto.TickerListDto;
import com.kms.algotrade.trade.dto.UpbitAccountDto;
import com.kms.algotrade.trade.entity.UpbitProperties;
import com.kms.algotrade.trade.listener.UpbitWebsocketListener;
import com.kms.algotrade.trade.repository.*;
import com.kms.algotrade.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService{

    private final CryptoExchangeInfoRepository cryptoExchangeInfoRepository;
    private final TickerListRepository tickerListRepository;
    private final CryptoExchangeInfoRepositoryCustom cryptoExchangeInfoRepositoryCustom;
    private final TickerListRepositoryCustom tickerListRepositoryCustom;
    private final UpbitPropertiesRepository upbitPropertiesRepository;
    private final UpbitWebsocketListener webSocketListener;
    public TradeServiceImpl(CryptoExchangeInfoRepository cryptoExchangeInfoRepository,
                            TickerListRepository tickerListRepository,
                            CryptoExchangeInfoRepositoryCustom cryptoExchangeInfoRepositoryCustom,
                            TickerListRepositoryCustom tickerListRepositoryCustom,
                            UpbitPropertiesRepository upbitPropertiesRepository,
                            UpbitWebsocketListener webSocketListener) {
        this.cryptoExchangeInfoRepository = cryptoExchangeInfoRepository;
        this.tickerListRepository = tickerListRepository;
        this.cryptoExchangeInfoRepositoryCustom = cryptoExchangeInfoRepositoryCustom;
        this.tickerListRepositoryCustom = tickerListRepositoryCustom;
        this.upbitPropertiesRepository = upbitPropertiesRepository;
        this.webSocketListener = webSocketListener;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMainPageData() {
        List<TickerListDto> tickerListDtos = tickerListRepositoryCustom.getMainPageTickerList();
        List<CryptoExchangeInfoDto> cryptoExchangeInfoDtos = cryptoExchangeInfoRepositoryCustom.getMainPageCryptoExchangeInfoList();

        Map<String, Object> mainPageDataMap = new HashMap<>();
        mainPageDataMap.put("tickerListDtos", tickerListDtos);
        mainPageDataMap.put("cryptoExchangeInfoDtos", cryptoExchangeInfoDtos);

        return mainPageDataMap;
    }
    @Override
    @Transactional(readOnly = true)
    public List<UpbitAccountDto> getUpbitAccountInfo() {

        List<UpbitAccountDto> accountInfoDtoList = new ArrayList<>();

        // 일단 내껄로만 테스트
        Optional<UpbitProperties> upOpt = upbitPropertiesRepository.findById(1);
        UpbitProperties up = upOpt.get();
        String accessKey = up.getAccessKey();
        String secretKey = up.getSecretKey();
        String serverUrl = up.getServerUrl();

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", authenticationToken)
                    .url(serverUrl + "/v1/accounts")
                    .build(); //GET Request

            //동기 처리시 execute함수 사용
            Response response = client.newCall(request).execute();
            //출력
            String message = response.body().string();
            accountInfoDtoList = JsonUtil.fromJson(message, List.class);
            log.info(accountInfoDtoList.toString());
        } catch (Exception e){
            log.info(e.toString());
        }
        return accountInfoDtoList;
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

    // 첫 시작 Rsi properties au, ad, lastPrice
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
        List<Map<String, Object>> resBodyMapList = null;
        Request request = null;
        Response response = null;
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

        log.info("cal get Time :: {}", ldtLastDay.toString());

        double au = 0.0;
        double ad = 0.0;
        double lastPrice = 0.0;

        for (int i = 0; i < resultMap.size()-2; i++) {

            String tdy = ldtLastDay.plusDays(i).format(dateTimeFormatter);
            String tmr = ldtLastDay.plusDays(i+1).format(dateTimeFormatter);

            log.info("tmr :: {}", tmr);
            double tdyPrice = resultMap.get(tdy);
            log.info("tdyPrice :: {}", tdyPrice);
            double tmrPrice = resultMap.get(tmr);
            log.info("tmrPrice :: {}", tmrPrice);
            double diffPrice = tmrPrice - tdyPrice;

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
