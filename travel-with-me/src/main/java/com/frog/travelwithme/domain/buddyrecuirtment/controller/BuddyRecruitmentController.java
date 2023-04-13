package com.frog.travelwithme.domain.buddyrecuirtment.controller;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.mapper.BuddyMapper;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentService;
import com.frog.travelwithme.global.dto.SingleResponseDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recruitments")
public class BuddyRecruitmentController {

    private final BuddyRecruitmentService buddyRecruitmentService;

    private final BuddyMapper buddyMapper;

    @PostMapping
    public ResponseEntity postBuddyRecruitment(@RequestBody BuddyDto.PostRecruitment postRecruitmentDto,
                                               @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        BuddyDto.ResponseRecruitment response = buddyRecruitmentService.createdRecruitment(postRecruitmentDto, email);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }
}
