package com.kms.algotrade.trade.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "upbit_transaction_history")
@Getter
@Setter
public class UpbitTransactionHistory {

    @Id
    @Column(name = "upbit_transaction_history_seq")
    private int upbitTransactionHistorySeq;

    @Column(name = "account_seq")
    private int accountSeq;

    @Column(name = "side")
    private String side;

    @Column(name = "price")
    private Double price;

    @Column(name = "market")
    private String market;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "remaining_volume")
    private Double remainingVolume;
}
