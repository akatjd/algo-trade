package com.kms.algotrade.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ticker_list")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TickerList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker_list_seq")
    private Integer tickerListSeq;

    @ManyToOne(targetEntity = CryptoExchangeInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_exchange_info_seq")
    private Integer cryptoExchangeInfoSeq;

    @Column(name = "ticker_krw")
    private String tickerKrw;

    @Column(name = "ticker_Btc")
    private String tickerBtc;

    @Column(name = "ticker_name")
    private String tickerName;
}
