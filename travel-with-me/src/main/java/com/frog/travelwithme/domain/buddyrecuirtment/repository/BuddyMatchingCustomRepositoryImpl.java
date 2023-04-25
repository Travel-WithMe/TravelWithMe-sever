package com.frog.travelwithme.domain.buddyrecuirtment.repository;

import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.QBuddyMatching;
import com.frog.travelwithme.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.frog.travelwithme.domain.buddyrecuirtment.entity.QBuddyMatching.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Repository
@RequiredArgsConstructor
public class BuddyMatchingCustomRepositoryImpl implements BuddyMatchingCustomRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<BuddyMatching> findBuddyMatchingByMemberAndBuddyRecruitment(Member member,
                                                                               BuddyRecruitment buddyRecruitment) {

        return Optional.ofNullable(queryFactory
                .from(buddyMatching)
                .select(buddyMatching)
                .where(
                        buddyMatching.member.eq(member)
                                .and(buddyMatching.buddyRecruitment.eq(buddyRecruitment))
                )
                .fetchOne());
    }
}
