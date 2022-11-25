package com.kms.algotrade.trade.service;

import com.kms.algotrade.trade.dto.UpbitAccountDto;

import java.util.List;
import java.util.Map;

public interface TradeService {
    Map<String, Object> getMainPageData();

    List<UpbitAccountDto> getUpbitAccountInfo();
}
