package com.kms.algotrade.trade.service;

import com.kms.algotrade.trade.dto.CalRsiPropertiesDto;

import java.util.List;

public interface WebSocketService {
    void conUpbitWebsocket(String url, List<String> tickerList);
    CalRsiPropertiesDto getUpbitBtcKrwCalRsiProperties();
}
