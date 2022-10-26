package com.kms.algotrade.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "crypto_exchange_info")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CryptoExchangeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crypto_exchange_info_seq")
    private Integer cryptoExchangeInfoSeq;

    @Column(name = "crypto_exchange_name")
    private String cryptoExchangeName;

    @Column(name = "crypto_exchange_server_url")
    private String cryptoExchangeServerUrl;
}
