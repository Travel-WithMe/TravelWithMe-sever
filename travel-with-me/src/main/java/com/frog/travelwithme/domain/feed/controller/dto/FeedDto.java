package com.frog.travelwithme.domain.feed.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        List<String> removeImageUrls;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        Long id;
        String nickname;
        String profileImage;
        String contents;
        String location;
        long likeCount;
        long commentCount;
        @JsonProperty("isWriter")
        boolean writer;
        @JsonProperty("isLiked")
        boolean liked;
        LocalDateTime createdAt;
        List<String> tags;
        List<String> imageUrls;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ResponseDetail {
        Long id;
        String contents;
        String nickName;
        String prfileImage;
        boolean isWriter;
        List<String> tags;
        // TODO: 댓글 구현 고민
        List<String> comments;
        List<String> imageUrls;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InternalPatch {
        String contents;
        String location;
        List<String> tags;
        List<String> removeImageUrls;
    }
}
