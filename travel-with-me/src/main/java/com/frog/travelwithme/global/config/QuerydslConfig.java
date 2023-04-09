package com.frog.travelwithme.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * QuerydslConfig 설명: Querydsl 사용을 위한 Bean 등록
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/09
 **/

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
