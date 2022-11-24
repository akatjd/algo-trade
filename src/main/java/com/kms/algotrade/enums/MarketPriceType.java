package com.kms.algotrade.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MarketPriceType implements EnumInterface{
    TICKER("ticker", "티커"),
    TRADE("trade", "트레이드"),
    ORDERBOOK("orderbook", "오더북");

    private MarketPriceType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private String type;
    private String name;

    public static MarketPriceType find(String type) {
        return EnumInterface.find(type, values());
    }

    @JsonCreator
    public static MarketPriceType findToNull(String type) {
        return EnumInterface.findToNull(type, values());
    }
}
