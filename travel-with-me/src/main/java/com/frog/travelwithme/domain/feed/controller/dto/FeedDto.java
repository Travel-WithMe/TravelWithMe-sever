package com.frog.travelwithme.domain.feed.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/18
 **/
public class FeedDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        @NotNull
        String contents;
        String location;
        List<String> tags;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        String contents;
        String location;
        List<String> tags;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        String nickName;
        String profileImage;
        String contents;
        String location;
        long likeCount;
        long commentCount;
        boolean isLiked;
        LocalDateTime createdAt;
        List<String> tags;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ResponseDetail {
        String nickName;
        String prfileImage;
        String contents;
        List<String> tags;
        // TODO: 댓글 구현 고민
        List<String> comments;
    }
}
