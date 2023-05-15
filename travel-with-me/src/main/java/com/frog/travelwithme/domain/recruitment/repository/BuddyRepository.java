package com.frog.travelwithme.domain.recruitment.repository;

import com.frog.travelwithme.domain.recruitment.entity.Buddy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface BuddyRepository extends JpaRepository<Buddy, Long>, BuddyCustomRepository {



}
