package com.kms.algotrade.trade.service;

import com.kms.algotrade.trade.dto.UpbitAccountDto;

import java.util.List;
import java.util.Map;

public interface TradeService {
    public Map<String, Object> getMainPageData();

    public List<UpbitAccountDto> getUpbitAccountInfo();
}
