package com.kms.algotrade.trade.repository;

import com.kms.algotrade.trade.entity.UpbitProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpbitPropertiesRepository extends JpaRepository<UpbitProperties, Integer> {

}
