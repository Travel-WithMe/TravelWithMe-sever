package com.frog.travelwithme.domain.recruitment.controller;

import com.frog.travelwithme.domain.recruitment.service.BuddyService;
import com.frog.travelwithme.global.dto.MessageResponseDto;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class BuddyController {

    private final BuddyService buddyService;

    @PostMapping("/{recruitment-id}/buddy/request")
    public ResponseEntity requestBuddy(@Positive @PathVariable("recruitment-id") Long recruitmentsId,
                                       @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = buddyService.requestBuddyByEmail(recruitmentsId, email);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }

    @PostMapping("/{recruitment-id}/buddy/cancel")
    public ResponseEntity cancelBuddy(@Positive @PathVariable("recruitment-id") Long recruitmentsId,
                                      @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = buddyService.cancelBuddyByEmail(recruitmentsId, email);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }

    @PostMapping("/{recruitment-id}/buddy/{buddy-id}/approve")
    public ResponseEntity approveBuddy(@Positive @PathVariable("recruitment-id") Long recruitmentsId,
                                       @Positive @PathVariable("buddy-id") Long buddyId,
                                       @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = buddyService.approveBuddyByEmail(recruitmentsId, email, buddyId);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }
}
