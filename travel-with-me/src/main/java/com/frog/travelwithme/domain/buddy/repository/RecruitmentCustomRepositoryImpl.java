package com.frog.travelwithme.domain.buddy.repository;

import com.frog.travelwithme.domain.member.entity.QMember;
import com.frog.travelwithme.domain.buddy.entity.QRecruitment;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.frog.travelwithme.domain.member.entity.QMember.*;
import static com.frog.travelwithme.domain.buddy.entity.QRecruitment.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Repository
@RequiredArgsConstructor
public class RecruitmentCustomRepositoryImpl implements RecruitmentCustomRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Recruitment> findRecruitmentById(Long id) {
        return Optional.ofNullable(queryFactory
                .from(recruitment)
                .select(recruitment)
                .leftJoin(recruitment.member, member).fetchJoin()
                .where(recruitment.id.eq(id))
                .fetchOne());
    }
}
