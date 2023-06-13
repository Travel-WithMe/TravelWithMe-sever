package com.frog.travelwithme.domain.common.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CommentDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        @Positive
        @Max(value = 2)
        private Integer depth;

        @NotNull
        private String content;

        private Long taggedMemberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {

        private String content;

        private Long taggedMemberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostResponse {
        private Long commentId;
        private Integer depth;
        private String content;
        private Long taggedMemberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PatchResponse {
        private Long commentId;
        private Integer depth;
        private String content;
        private Long taggedMemberId;
    }
}
