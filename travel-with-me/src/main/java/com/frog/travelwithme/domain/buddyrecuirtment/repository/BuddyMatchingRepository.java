package com.frog.travelwithme.domain.buddyrecuirtment.repository;

import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface BuddyMatchingRepository extends JpaRepository<BuddyMatching, Long>, BuddyMatchingCustomRepository {



}
