package com.frog.travelwithme.domain.common.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

        private Long groupId;

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
        private Long groupId;
        private String content;
        private Long taggedMemberId;
        private String taggedMemberNickname;
        @JsonProperty("isDeleted")
        private boolean deleted;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PatchResponse {
        private Long commentId;
        private Integer depth;
        private Long groupId;
        private String content;
        private Long taggedMemberId;
        private String taggedMemberNickname;
        @JsonProperty("isDeleted")
        private boolean deleted;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeleteResponse {
        private Long commentId;
        private Integer depth;
        private Long groupId;
        private String content;
        @JsonProperty("isDeleted")
        private boolean deleted;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetResponse {
        private Long commentId;
        private Integer depth;
        private Long groupId;
        private String content;
        @JsonProperty("isDeleted")
        private boolean deleted;
    }
}
