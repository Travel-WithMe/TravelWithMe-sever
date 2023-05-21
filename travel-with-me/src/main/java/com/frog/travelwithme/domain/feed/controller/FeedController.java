package com.frog.travelwithme.domain.feed.controller;

import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.feed.controller.dto.TagDto;
import com.frog.travelwithme.domain.feed.service.FeedService;
import com.frog.travelwithme.domain.feed.service.TagService;
import com.frog.travelwithme.global.dto.MessageResponseDto;
import com.frog.travelwithme.global.dto.PagelessMultiResponseDto;
import com.frog.travelwithme.global.dto.SingleResponseDto;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final TagService tagService;

    @PostMapping
    public ResponseEntity postFeed(@RequestPart(value = "files") List<MultipartFile> multipartFiles,
                                   @Valid @RequestPart(value = "data") FeedDto.Post postDto,
                                   @AuthenticationPrincipal CustomUserDetails user) {
        FeedDto.Response response = feedService.postFeed(user.getEmail(), postDto, multipartFiles);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @GetMapping("/{feed-id}")
    public ResponseEntity getFeed(@PathVariable("feed-id") Long feedId,
                                  @AuthenticationPrincipal CustomUserDetails user) {
        FeedDto.Response response = feedService.findFeedById(user.getEmail(), feedId);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllFeed(@RequestParam(required = false) Long lastFeedId,
                                     @AuthenticationPrincipal CustomUserDetails user) {
        // TODO: 팔로잉, 관심사 태그 기반 검색 알고리즘 고민
        List<FeedDto.Response> responseList = feedService.findAll(lastFeedId, user.getEmail());

        return new ResponseEntity<>(new PagelessMultiResponseDto<>(responseList), HttpStatus.OK);
    }

    @PatchMapping("/{feed-id}")
    public ResponseEntity patchFeed(@PathVariable("feed-id") Long feedId,
                                    @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                                    @Valid @RequestPart(value = "data") FeedDto.Patch patchDto,
                                    @AuthenticationPrincipal CustomUserDetails user) {
        FeedDto.Response response = feedService.updateFeed(user.getEmail(), feedId, patchDto, multipartFiles);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{feed-id}")
    public ResponseEntity deleteFeed(@PathVariable("feed-id") Long feedId,
                                     @AuthenticationPrincipal CustomUserDetails user) {
        feedService.deleteFeed(user.getEmail(), feedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity getTagsByNameLike(@RequestParam String tagName,
                                            @RequestParam(required = false) int size) {
        List<TagDto.Response> responseList = tagService.findTagsStartWith(tagName, size);

        return new ResponseEntity<>(new PagelessMultiResponseDto<>(responseList), HttpStatus.OK);
    }

    @PostMapping("/{feed-id}/likes")
    public ResponseEntity<EnumCollection.ResponseBody> postLike(@PathVariable("feed-id") Long feedId,
                                                                @AuthenticationPrincipal CustomUserDetails user) {
        EnumCollection.ResponseBody responseBody = feedService.doLike(user.getEmail(), feedId);

        return new ResponseEntity(new MessageResponseDto(responseBody.getDescription()), HttpStatus.OK);
    }

    @DeleteMapping("/{feed-id}/likes")
    public ResponseEntity<EnumCollection.ResponseBody> deleteLike(@PathVariable("feed-id") Long feedId,
                                                                  @AuthenticationPrincipal CustomUserDetails user) {
        EnumCollection.ResponseBody responseBody = feedService.cancelLike(user.getEmail(), feedId);

        return new ResponseEntity(new MessageResponseDto(responseBody.getDescription()), HttpStatus.OK);
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
