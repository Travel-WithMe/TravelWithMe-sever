package com.frog.travelwithme.domain.member.repository;

import com.frog.travelwithme.domain.member.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Member 설명: 회원 데이터 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/29
 **/
public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByType(String interestType);
}
