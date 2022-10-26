package com.kms.algotrade.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_properties")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_properties")
    private Integer accountPropertiesSeq;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_seq")
    private Integer accountSeq;

    @ManyToOne(targetEntity = CryptoExchangeInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_exchange_info_seq")
    private Integer cryptoExchangeInfoSeq;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "account_properties_status")
    private String accountPropertiesStatus;

    @CreationTimestamp
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;
}
