package com.kms.algotrade.trade.service;

import com.kms.algotrade.trade.dto.CryptoExchangeInfoDto;
import com.kms.algotrade.trade.dto.TickerListDto;
import com.kms.algotrade.trade.repository.CryptoExchangeInfoRepository;
import com.kms.algotrade.trade.repository.CryptoExchangeInfoRepositoryCustom;
import com.kms.algotrade.trade.repository.TickerListRepository;
import com.kms.algotrade.trade.repository.TickerListRepositoryCustom;
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
    @Transactional(readOnly = true)
    public Map<String, Object> getMainPageData(){
        List<TickerListDto> tickerListDtos = tickerListRepositoryCustom.getMainPageTickerList();
        List<CryptoExchangeInfoDto> cryptoExchangeInfoDtos = cryptoExchangeInfoRepositoryCustom.getMainPageCryptoExchangeInfoList();

        Map<String, Object> mainPageDataMap = new HashMap<>();
        mainPageDataMap.put("tickerListDtos", tickerListDtos);
        mainPageDataMap.put("cryptoExchangeInfoDtos", cryptoExchangeInfoDtos);

        return mainPageDataMap;
    }
}
