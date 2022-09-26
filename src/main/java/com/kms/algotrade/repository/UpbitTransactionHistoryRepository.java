package com.kms.algotrade.repository;

import com.kms.algotrade.entity.UpbitTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpbitTransactionHistoryRepository extends JpaRepository<UpbitTransactionHistory, Integer> {

}
