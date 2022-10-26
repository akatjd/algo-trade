package com.kms.algotrade.repository;

import com.kms.algotrade.entity.TickerList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerListRepository extends JpaRepository<TickerList, Integer> {
}
