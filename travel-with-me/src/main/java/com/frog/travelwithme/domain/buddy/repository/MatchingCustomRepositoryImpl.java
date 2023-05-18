package com.frog.travelwithme.domain.buddy.repository;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.QMatching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.frog.travelwithme.domain.buddy.entity.QMatching.*;
import static com.frog.travelwithme.domain.buddy.entity.QRecruitment.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Repository
@RequiredArgsConstructor
public class MatchingCustomRepositoryImpl implements MatchingCustomRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Matching> findMatchingByIdJoinRecruitment(Long id) {
        return Optional.ofNullable(queryFactory
                .from(matching)
                .select(matching)
                .leftJoin(matching.recruitment, recruitment).fetchJoin()
                .where(
                        matching.id.eq(id)
                )
                .fetchOne());
    }

    @Override
    public Optional<Matching> findMatchingByMemberAndRecruitment(Member member,
                                                                Recruitment recruitment) {

        return Optional.ofNullable(queryFactory
                .from(matching)
                .select(matching)
                .where(
                        matching.member.eq(member)
                                .and(matching.recruitment.eq(recruitment))
                )
                .fetchOne());
    }
}
