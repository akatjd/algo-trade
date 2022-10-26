package com.kms.algotrade.repository;

import com.kms.algotrade.entity.CryptoExchangeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoExchangeInfoRepository extends JpaRepository<CryptoExchangeInfo, Integer> {
}
