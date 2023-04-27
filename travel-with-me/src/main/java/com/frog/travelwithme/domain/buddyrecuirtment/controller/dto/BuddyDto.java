package com.frog.travelwithme.domain.buddyrecuirtment.controller.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public static class PostRecruitment {
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
    public static class PatchRecruitment {
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
    public static class PostResponseRecruitment {
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
    public static class PatchResponseRecruitment {
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
    public static class GetResponseRecruitment {
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
        private List<RecruitmentMember> recruitmentMembers;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitmentMember {
        private Long id;
        private String nickname;
        private String image;
    }
}
