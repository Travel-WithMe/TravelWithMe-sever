package com.frog.travelwithme.domain.buddyrecuirtment.service;


import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

public interface BuddyRecruitmentService {

    BuddyDto.ResponseRecruitment createdRecruitment(BuddyDto.PostRecruitment postRecruitment, String email);
}
