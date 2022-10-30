package com.kms.algotrade.service;

import com.kms.algotrade.dto.CryptoExchangeInfoDto;
import com.kms.algotrade.dto.TickerListDto;
import com.kms.algotrade.dto.TradeMainDto;
import com.kms.algotrade.repository.CryptoExchangeInfoRepository;
import com.kms.algotrade.repository.CryptoExchangeInfoRepositoryCustom;
import com.kms.algotrade.repository.TickerListRepository;
import com.kms.algotrade.repository.TickerListRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService{
    @Autowired
    CryptoExchangeInfoRepository cryptoExchangeInfoRepository;

    @Autowired
    TickerListRepository tickerListRepository;

    @Autowired
    CryptoExchangeInfoRepositoryCustom cryptoExchangeInfoRepositoryCustom;

    @Autowired
    TickerListRepositoryCustom tickerListRepositoryCustom;

    @Override
    public Map<String, Object> getMainPageData(){
        return getTradeMainData();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getTradeMainData() {

        List<TickerListDto> tickerListDtos = tickerListRepositoryCustom.getMainPageTickerList();
        List<CryptoExchangeInfoDto> cryptoExchangeInfoDtos = cryptoExchangeInfoRepositoryCustom.getMainPageCryptoExchangeInfoList();

        Map<String, Object> mainPageDataMap = new HashMap<>();
        mainPageDataMap.put("tickerListDtos", tickerListDtos);
        mainPageDataMap.put("cryptoExchangeInfoDtos", cryptoExchangeInfoDtos);

        return mainPageDataMap;
    }
}
