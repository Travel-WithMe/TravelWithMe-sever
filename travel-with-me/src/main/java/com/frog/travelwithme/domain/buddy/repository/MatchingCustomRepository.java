package com.frog.travelwithme.domain.buddy.repository;


import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface MatchingCustomRepository {

    Optional<Matching> findMatchingById(Long id);

    Optional<Matching> findMatchingByMemberAndRecruitment(Member member, Recruitment recruitment);

}
