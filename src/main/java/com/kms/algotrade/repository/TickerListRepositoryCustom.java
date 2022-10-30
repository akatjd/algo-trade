package com.kms.algotrade.repository;

import com.kms.algotrade.dto.TickerListDto;
import com.kms.algotrade.entity.QTickerList;
import com.kms.algotrade.entity.TickerList;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TickerListRepositoryCustom extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public TickerListRepositoryCustom(JPAQueryFactory jpaQueryFactory) {
        super(TickerList.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QTickerList tickerList = new QTickerList("tl");

    public List<TickerListDto> getMainPageTickerList() {
        return jpaQueryFactory.select(Projections.fields(TickerListDto.class,
                        tickerList.tickerSeq, tickerList.tickerName))
                .from(tickerList)
                .fetch();
    }

}
