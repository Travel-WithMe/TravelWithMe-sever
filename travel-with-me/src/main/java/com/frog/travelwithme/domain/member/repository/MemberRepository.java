package com.frog.travelwithme.domain.member.repository;

import com.frog.travelwithme.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);

    Optional<Member> findByNickname(String nickname);
}
