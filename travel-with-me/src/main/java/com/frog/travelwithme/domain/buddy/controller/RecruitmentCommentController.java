package com.frog.travelwithme.domain.buddy.controller;

import com.frog.travelwithme.domain.buddy.service.RecruitmentCommentService;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.global.dto.SingleResponseDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/24
 **/

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/recruitments")
public class RecruitmentCommentController {

    private final RecruitmentCommentService recruitmentCommentService;

    @PostMapping("/{recruitment-id}/comments")
    public ResponseEntity postComment(@Positive @PathVariable("recruitment-id") Long recruitmentId,
                                      @Valid @RequestBody CommentDto.Post postDto,
                                      @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        CommentDto.PostResponse response =
                recruitmentCommentService.createCommentByEmail(postDto, recruitmentId, email);

        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

}
