package com.frog.travelwithme.domain.buddyrecuirtment.controller;

import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyMatchingService;
import com.frog.travelwithme.global.dto.MessageResponseDto;
import com.frog.travelwithme.global.dto.SingleResponseDto;
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
@RequestMapping("/matching")
public class BuddyMatchingController {

    private final BuddyMatchingService buddyMatchingService;

    @PostMapping("/{recruitmentId-id}")
    public ResponseEntity requestMatching(@Positive @PathVariable("recruitmentId-id") Long recruitmentsId,
                                          @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = buddyMatchingService.requestMatching(recruitmentsId, email);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }

}
