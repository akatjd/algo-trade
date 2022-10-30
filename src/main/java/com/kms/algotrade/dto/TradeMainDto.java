package com.kms.algotrade.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Map;

@Getter
@Setter
public class TradeMainDto {

    private Map<Integer, String> cryptoExchangeMap;

    private Map<Integer, String> cryptoMap;

    private TradeMainDto(Builder builder) {
        this.cryptoExchangeMap = builder.cryptoExchangeMap;
        this.cryptoMap = builder.cryptoMap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<Integer, String> cryptoExchangeMap;

        private Map<Integer, String> cryptoMap;

        public Builder() {}

        public Builder cryptoExchangeMap(Map<Integer, String> cryptoExchangeMap) {
            this.cryptoExchangeMap = cryptoExchangeMap;
            return this;
        }

        public Builder cryptoMap(Map<Integer, String> cryptoMap) {
            this.cryptoMap = cryptoMap;
            return this;
        }
        public TradeMainDto build() {
            return new TradeMainDto(this);
        }
    }
}
