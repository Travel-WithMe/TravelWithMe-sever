package com.frog.travelwithme.domain.buddyrecuirtment.repository;


import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface BuddyMatchingCustomRepository {

    Optional<BuddyMatching> findBuddyMatchingByMemberAndBuddyRecruitment(Member member, BuddyRecruitment buddyRecruitment);

}
