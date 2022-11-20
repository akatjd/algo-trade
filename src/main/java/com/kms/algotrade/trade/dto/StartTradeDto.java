package com.kms.algotrade.trade.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StartTradeDto {
    private Integer buyRsi;
    private Integer sellRsi;
    private Integer selExchange;
    private Integer selTicker;

    @Override
    public String toString() {
        return "StartTradeDto{" +
                "buyRsi=" + buyRsi +
                ", sellRsi=" + sellRsi +
                ", selExchange=" + selExchange +
                ", selTicker=" + selTicker +
                '}';
    }
}
