package com.frog.travelwithme.domain.buddy.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.frog.travelwithme.global.validation.CustomAnnotationCollection.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/12
 **/

public class BuddyDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitmentPost {
        @NotNull
        private String title;

        @NotNull
        private String content;

        @NotNull
        private String travelNationality;

        @NotNull
        private String travelStartDate;

        @NotNull
        private String travelEndDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitmentPatch {
        private String title;
        private String content;
        private String travelNationality;
        private String travelStartDate;
        private String travelEndDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitmentPostResponse {
        private Long id;
        private String title;
        private String content;
        private String travelNationality;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;
        private Long viewCount;
        private Long commentCount;
        private String nickname;
        private String memberImage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitmentPatchResponse {
        private Long id;
        private String title;
        private String content;
        private String travelNationality;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitmentGetResponse {
        private Long id;
        private String title;
        private String content;
        private String travelNationality;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;
        private Long viewCount;
        private Long commentCount;
        private String nickname;
        private String memberImage;
        private LocalDateTime createdAt;
        private Boolean recruitmentRequestStatus;
        private List<MatchingMemberResponse> members;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MatchingMemberResponse {
        private Long id;
        private String nickname;
        private String image;
    }
}
