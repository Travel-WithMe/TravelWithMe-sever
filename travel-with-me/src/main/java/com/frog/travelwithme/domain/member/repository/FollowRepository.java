package com.frog.travelwithme.domain.member.repository;

import com.frog.travelwithme.domain.member.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * FollowService 설명: 팔로우 데이터 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/06/02
 **/
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
