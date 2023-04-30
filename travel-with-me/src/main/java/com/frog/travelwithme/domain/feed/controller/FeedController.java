package com.frog.travelwithme.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    @PostMapping
    public ResponseEntity postFeed() {
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{feed-id}")
    public ResponseEntity getFeed() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllFeed() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/{feed-id}")
    public ResponseEntity patchFeed() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{feed-id}")
    public ResponseEntity deleteFeed() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{feed-id}/tags")
    public ResponseEntity postTag() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity getTag() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{feed-id}/tags")
    public ResponseEntity deleteTag() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{feed-id}/likes")
    public ResponseEntity postLike() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{feed-id}/likes")
    public ResponseEntity deleteLike() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{feed-id}/comments")
    public ResponseEntity postComment() {
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/{feed-id}/comments")
    public ResponseEntity patchComment() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{feed-id}/comments")
    public ResponseEntity deleteComment() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
