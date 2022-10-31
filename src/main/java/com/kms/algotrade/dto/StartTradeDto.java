package com.kms.algotrade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StartTradeDto {
    private String accountId;
    private Integer buyRsi;
    private Integer sellRsi;
    private Integer cryptoExchangeInfoSeq;
    private Integer tickerInfoSeq;
}
