package com.frog.travelwithme.domain.recruitment.repository;

import com.frog.travelwithme.domain.recruitment.entity.Buddy;
import com.frog.travelwithme.domain.recruitment.entity.QBuddy;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.frog.travelwithme.domain.recruitment.entity.QBuddy.*;
import static com.frog.travelwithme.domain.recruitment.entity.QRecruitment.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Repository
@RequiredArgsConstructor
public class BuddyCustomRepositoryImpl implements BuddyCustomRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Buddy> findBuddyByIdJoinRecruitment(Long id) {
        return Optional.ofNullable(queryFactory
                .from(buddy)
                .select(buddy)
                .leftJoin(buddy.recruitment, recruitment).fetchJoin()
                .where(
                        buddy.id.eq(id)
                )
                .fetchOne());
    }

    @Override
    public Optional<Buddy> findBuddyByMemberAndRecruitment(Member member,
                                                          Recruitment buddyRecruitment) {

        return Optional.ofNullable(queryFactory
                .from(buddy)
                .select(buddy)
                .where(
                        buddy.member.eq(member)
                                .and(buddy.recruitment.eq(buddyRecruitment))
                )
                .fetchOne());
    }
}
