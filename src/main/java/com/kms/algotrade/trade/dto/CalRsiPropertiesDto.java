package com.kms.algotrade.trade.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalRsiPropertiesDto {
    Double avgUp;

    Double avgDown;

    Double lastPrice;

    String lastDay;
}
