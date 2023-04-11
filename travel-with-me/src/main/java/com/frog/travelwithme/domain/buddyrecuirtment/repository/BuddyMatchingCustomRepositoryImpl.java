package com.frog.travelwithme.domain.buddyrecuirtment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Repository
@RequiredArgsConstructor
public class BuddyMatchingCustomRepositoryImpl implements BuddyMatchingCustomRepository{

    private final JPAQueryFactory queryFactory;


}
