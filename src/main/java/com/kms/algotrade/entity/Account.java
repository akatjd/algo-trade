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
@Table(name = "account")
@Getter
@Setter
public class Account {

    @Id
    @Column(name = "account_seq")
    private Integer accountSeq;

    @Column(name = "account_id", unique = true)
    private String accountId;

    @Column(nullable = false, length = 300, name = "password")
    private String password;

    @Column(nullable = true, length = 45, name = "email")
    private String email;

    @Column(nullable = true, length = 13, name = "phone_number")
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(nullable = false, length = 1)
    private String accountStatus;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = true, length = 20)
    private String updatedBy;

    @Column(nullable = true, length = 15)
    private String role;
}

