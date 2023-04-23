package com.frog.travelwithme.domain.buddyrecuirtment.controller;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentService;
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
public class BuddyRecruitmentController {

    private final BuddyRecruitmentService buddyRecruitmentService;

    @PostMapping
    public ResponseEntity postBuddyRecruitment(@Valid @RequestBody BuddyDto.PostRecruitment postRecruitmentDto,
                                               @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        BuddyDto.PostResponseRecruitment response = buddyRecruitmentService.createBuddyRecruitment(
                postRecruitmentDto, email
        );

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{recruitments-id}")
    public ResponseEntity patchBuddyRecruitment(@Positive @PathVariable("recruitments-id") Long recruitmentsId,
                                                @RequestBody BuddyDto.PatchRecruitment patchRecruitmentDto,
                                                @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        BuddyDto.PatchResponseRecruitment response = buddyRecruitmentService.updateBuddyRecruitment(
                patchRecruitmentDto, recruitmentsId, email
        );
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/{recruitments-id}/deleted")
    public ResponseEntity deleteBuddyRecruitment(@Positive @PathVariable("recruitments-id") Long recruitmentsId,
                                                 @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        buddyRecruitmentService.deleteBuddyRecruitment(recruitmentsId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{recruitments-id}")
    public ResponseEntity getBuddyRecruitment(@Positive @PathVariable("recruitments-id") Long recruitmentsId,
                                              @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        BuddyDto.GetResponseRecruitment response = buddyRecruitmentService.findBuddyRecruitment(recruitmentsId, email);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
