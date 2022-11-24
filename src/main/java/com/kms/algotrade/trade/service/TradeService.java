package com.kms.algotrade.trade.service;

import com.kms.algotrade.trade.dto.CalRsiPropertiesDto;
import com.kms.algotrade.trade.dto.UpbitAccountDto;

import java.util.List;
import java.util.Map;

public interface TradeService {
    public Map<String, Object> getMainPageData();

    public List<UpbitAccountDto> getUpbitAccountInfo();

    public void conUpbitWebsocket(String url, List<String> tickerList);

    public CalRsiPropertiesDto getUpbitBtcKrwCalRsiProperties();
}
