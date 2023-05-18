package com.frog.travelwithme.domain.buddy.controller;

import com.frog.travelwithme.domain.buddy.service.MatchingService;
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
public class MatchingController {


    private final MatchingService matchingService;

    @PostMapping("/{recruitment-id}/matching/request")
    public ResponseEntity requestMatching(@Positive @PathVariable("recruitment-id") Long recruitmentsId,
                                          @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = matchingService.requestMatchingByEmail(recruitmentsId, email);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }

    @PostMapping("/{recruitment-id}/matching/cancel")
    public ResponseEntity cancelMatching(@Positive @PathVariable("recruitment-id") Long recruitmentsId,
                                         @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = matchingService.cancelMatchingByEmail(recruitmentsId, email);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }

    @PostMapping("/{recruitment-id}/matching/{matching-id}/approve")
    public ResponseEntity approveMatching(@Positive @PathVariable("recruitment-id") Long recruitmentsId,
                                          @Positive @PathVariable("matching-id") Long matchingId,
                                          @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = matchingService.approveMatchingByEmail(recruitmentsId, email, matchingId);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }

    @PostMapping("/{recruitment-id}/matching/{matching-id}/reject")
    public ResponseEntity rejectMatching(@Positive @PathVariable("recruitment-id") Long recruitmentsId,
                                          @Positive @PathVariable("matching-id") Long matchingId,
                                          @AuthenticationPrincipal CustomUserDetails user) {

        String email = user.getEmail();
        EnumCollection.ResponseBody response = matchingService.rejectMatchingByEmail(recruitmentsId, email, matchingId);
        return new ResponseEntity<>(new MessageResponseDto(response.getDescription()), HttpStatus.OK);
    }
}
