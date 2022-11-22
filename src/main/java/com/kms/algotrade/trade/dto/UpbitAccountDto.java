package com.kms.algotrade.trade.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@ToString
public class UpbitAccountDto {
    String currency;

    Double balance;

    Double locked;

    Double avgBuyPrice;

    Boolean avgBuyPriceModified;

    String unitCurrency;
}
