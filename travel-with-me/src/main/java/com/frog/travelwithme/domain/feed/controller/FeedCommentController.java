package com.frog.travelwithme.domain.feed.controller;

import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.feed.service.FeedCommentService;
import com.frog.travelwithme.global.dto.PagelessMultiResponseDto;
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
import java.util.List;

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

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity deleteComment(@Positive @PathVariable("comment-id") Long commentId,
                                        @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        CommentDto.DeleteResponse response =
                feedCommentService.deleteCommentByEmail(commentId, email);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/{feed-id}/comments")
    public ResponseEntity getComments(@Positive @PathVariable("feed-id") Long feedId,
                                      @AuthenticationPrincipal CustomUserDetails user,
                                      @Positive @RequestParam(required = false) Long lastCommentId,
                                      @Positive @RequestParam int size) {
        String email = user.getEmail();
        List<CommentDto.GetResponse> responseList =
                feedCommentService.findAllCommentsByFeedId(feedId, email, lastCommentId, size);

        return new ResponseEntity<>(new PagelessMultiResponseDto<>(responseList), HttpStatus.OK);
    }

    @PostMapping("/comments/{comment-id}/likes")
    public ResponseEntity postLike(@Positive @PathVariable("comment-id") Long feedCommentId,
                                   @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        feedCommentService.doLike(email, feedCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comments/{comment-id}/likes")
    public ResponseEntity deleteLike(@Positive @PathVariable("comment-id") Long feedCommentId,
                                     @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        feedCommentService.cancelLike(email, feedCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
