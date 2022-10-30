package com.kms.algotrade.repository;

import com.kms.algotrade.dto.CryptoExchangeInfoDto;
import com.kms.algotrade.entity.CryptoExchangeInfo;
import com.kms.algotrade.entity.QCryptoExchangeInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CryptoExchangeInfoRepositoryCustom extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public CryptoExchangeInfoRepositoryCustom(JPAQueryFactory jpaQueryFactory) {
        super(CryptoExchangeInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QCryptoExchangeInfo qCryptoExchangeInfo = new QCryptoExchangeInfo("cei");

    public List<CryptoExchangeInfoDto> getMainPageCryptoExchangeInfoList() {
        return jpaQueryFactory.select(Projections.fields(CryptoExchangeInfoDto.class,
                                qCryptoExchangeInfo.cryptoExchangeInfoSeq, qCryptoExchangeInfo.cryptoExchangeName))
                .from(qCryptoExchangeInfo)
                .fetch();
    }
}
