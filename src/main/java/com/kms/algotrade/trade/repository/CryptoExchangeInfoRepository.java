package com.kms.algotrade.trade.repository;

import com.kms.algotrade.trade.entity.CryptoExchangeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoExchangeInfoRepository extends JpaRepository<CryptoExchangeInfo, Integer> {
}
