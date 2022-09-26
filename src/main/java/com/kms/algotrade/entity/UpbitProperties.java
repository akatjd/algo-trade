package com.kms.algotrade.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "upbit_properties")
@Getter
@Setter
public class UpbitProperties {

    @Id
    @Column(name = "upbit_properties_seq")
    private int upbitPropertiesSeq;

    @Column(name = "account_seq")
    private String accountSeq;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "server_url")
    private String serverUrl;

    @Column(name = "upbit_properties_status")
    private String upbitPropertiesStatus;

    @CreationTimestamp
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;
}

