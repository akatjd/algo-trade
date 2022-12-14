package com.kms.algotrade.account.repository;

import com.kms.algotrade.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByEmail(String email);

    Optional<Account> findByAccountId(String accountId);
    Optional<Account> findByRefreshToken(String refreshToken);
}

