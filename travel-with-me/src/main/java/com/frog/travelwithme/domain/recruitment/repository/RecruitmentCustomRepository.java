package com.frog.travelwithme.domain.recruitment.repository;


import com.frog.travelwithme.domain.recruitment.entity.Recruitment;

import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface RecruitmentCustomRepository {

    Optional<Recruitment> findRecruitmentByIdJoinMember(Long id);

}
