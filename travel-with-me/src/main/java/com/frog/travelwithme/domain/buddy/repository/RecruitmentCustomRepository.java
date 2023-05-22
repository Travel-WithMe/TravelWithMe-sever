package com.frog.travelwithme.domain.buddy.repository;


import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.global.enums.EnumCollection;

import java.util.List;
import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface RecruitmentCustomRepository {
    Optional<Recruitment> findRecruitmentByIdAndMatchingStatus(Long id, MatchingStatus status);
}
