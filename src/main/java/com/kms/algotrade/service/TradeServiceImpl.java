package com.kms.algotrade.service;

import com.kms.algotrade.dto.TradeMainDto;
import com.kms.algotrade.entity.CryptoExchangeInfo;
import com.kms.algotrade.repository.CryptoExchangeInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeServiceImpl implements TradeService{
    @Autowired
    CryptoExchangeInfoRepository cryptoExchangeInfoRepository;
    @Override
    public TradeMainDto getMainPageData(){

        List<CryptoExchangeInfo> cryptoExchangeInfoList = cryptoExchangeInfoRepository.findAll();



        return new TradeMainDto();
    }
}
