package com.frog.travelwithme.domain.buddy.controller;

import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.buddy.service.RecruitmentService;
import com.frog.travelwithme.global.dto.SingleResponseDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/recruitments")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public ResponseEntity postRecruitment(@Valid @RequestBody RecruitmentDto.Post postDto,
                                          @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        RecruitmentDto.PostResponse response = recruitmentService.createRecruitmentByEmail(
                postDto, email
        );

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{recruitment-id}")
    public ResponseEntity patchRecruitment(@Positive @PathVariable("recruitment-id") Long recruitmentId,
                                           @RequestBody RecruitmentDto.Patch patchDto,
                                           @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        RecruitmentDto.PatchResponse response = recruitmentService.updateRecruitmentByEmail(
                patchDto, recruitmentId, email
        );
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{recruitment-id}")
    public ResponseEntity deleteRecruitment(@Positive @PathVariable("recruitment-id") Long recruitmentId,
                                            @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        recruitmentService.deleteRecruitmentByEmail(recruitmentId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    TODO : 댓글, 매칭관련 작업이 끝나면 조회 반환 작성하기
//    @GetMapping("/{recruitment-id}")
//    public ResponseEntity getRecruitment(@Positive @PathVariable("recruitment-id") Long recruitmentId,
//                                         @AuthenticationPrincipal CustomUserDetails user) {
//
//        String email = user.getEmail();
//        RecruitmentDto.GetResponse response = recruitmentService.findRecruitment(recruitmentId, email);
//
//        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
//    }

}
