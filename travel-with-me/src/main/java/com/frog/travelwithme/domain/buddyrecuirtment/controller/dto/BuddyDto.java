package com.frog.travelwithme.domain.buddyrecuirtment.controller.dto;

import lombok.*;

import java.time.LocalDate;

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
    public static class ResponseRecruitment {
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
}
