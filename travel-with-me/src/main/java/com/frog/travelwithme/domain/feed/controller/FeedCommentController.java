package com.frog.travelwithme.domain.feed.controller;

import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.feed.service.FeedCommentService;
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
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/06/28
 **/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;

    @PostMapping("/{feed-id}/comments")
    public ResponseEntity postComment(@Positive @PathVariable("feed-id") Long feedId,
                                      @Valid @RequestBody CommentDto.Post postDto,
                                      @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        CommentDto.PostResponse response =
                feedCommentService.createCommentByEmail(postDto, feedId, email);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PatchMapping("/comments/{comment-id}")
    public ResponseEntity updateComment(@Positive @PathVariable("comment-id") Long commentId,
                                        @Valid @RequestBody CommentDto.Patch patchDto,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        CommentDto.PatchResponse response =
                feedCommentService.updateCommentByEmail(patchDto, commentId, email);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
