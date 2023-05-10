package com.frog.travelwithme.domain.buddy.repository;


import com.frog.travelwithme.domain.buddy.entity.Buddy;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.member.entity.Member;

import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface BuddyCustomRepository {

    Optional<Buddy> findBuddyByIdJoinRecruitment(Long id);

    Optional<Buddy> findBuddyByMemberAndRecruitment(Member member, Recruitment buddyRecruitment);

}
