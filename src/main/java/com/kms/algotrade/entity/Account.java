package com.kms.algotrade.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

//    @Builder
//    public Account(Integer accountSeq, String accountId,
//                   String password, String email,
//                   String phoneNumber) {
//        this.accountSeq = accountSeq;
//        this.accountId = accountId;
//        this.password = password;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//    }

    @Builder
    public Account(String accountId,
                   String password, String email,
                   String role) {
        this.accountId = accountId;
        this.password = password;
        this.email = email;
        this.role = role;
        this.accountStatus = "y";
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountSeq=" + accountSeq +
                ", accountId='" + accountId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registeredAt=" + registeredAt +
                ", accountStatus='" + accountStatus + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

