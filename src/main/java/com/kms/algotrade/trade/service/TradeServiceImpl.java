package com.kms.algotrade.trade.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kms.algotrade.trade.dto.CryptoExchangeInfoDto;
import com.kms.algotrade.trade.dto.TickerListDto;
import com.kms.algotrade.trade.dto.UpbitAccountDto;
import com.kms.algotrade.trade.entity.UpbitProperties;
import com.kms.algotrade.trade.repository.*;
import com.kms.algotrade.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService{

    private final CryptoExchangeInfoRepositoryCustom cryptoExchangeInfoRepositoryCustom;
    private final TickerListRepositoryCustom tickerListRepositoryCustom;
    private final UpbitPropertiesRepository upbitPropertiesRepository;
    public TradeServiceImpl(CryptoExchangeInfoRepositoryCustom cryptoExchangeInfoRepositoryCustom,
                            TickerListRepositoryCustom tickerListRepositoryCustom,
                            UpbitPropertiesRepository upbitPropertiesRepository) {
        this.cryptoExchangeInfoRepositoryCustom = cryptoExchangeInfoRepositoryCustom;
        this.tickerListRepositoryCustom = tickerListRepositoryCustom;
        this.upbitPropertiesRepository = upbitPropertiesRepository;
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
}
