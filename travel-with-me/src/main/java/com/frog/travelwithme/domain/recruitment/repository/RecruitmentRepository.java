package com.frog.travelwithme.domain.recruitment.repository;

import com.frog.travelwithme.domain.buddy.repository.BuddyCustomRepository;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentCustomRepository {

}
