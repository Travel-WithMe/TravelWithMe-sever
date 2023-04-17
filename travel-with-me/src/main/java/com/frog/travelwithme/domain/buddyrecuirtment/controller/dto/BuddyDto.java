package com.frog.travelwithme.domain.buddyrecuirtment.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public static class ResponseRecruitment {

        private final String title;
        private final String content;
        private final String travelNationality;
        private final LocalDate travelStartDate;
        private final LocalDate travelEndDate;
        private final Long viewCount;
        private final Long commentCount;
        private final String nickname;
        private final String memberImage;

        @JsonCreator
        private ResponseRecruitment(@JsonProperty("title") String title,
                                    @JsonProperty("content") String content,
                                    @JsonProperty("travelNationality") String travelNationality,
                                    @JsonProperty("travelStartDate") LocalDate travelStartDate,
                                    @JsonProperty("travelEndDate") LocalDate travelEndDate,
                                    @JsonProperty("viewCount") Long viewCount,
                                    @JsonProperty("commentCount") Long commentCount,
                                    @JsonProperty("nickname") String nickname,
                                    @JsonProperty("memberImage") String memberImage){
            this.title = title;
            this.content = content;
            this.travelNationality = travelNationality;
            this.travelStartDate = travelStartDate;
            this.travelEndDate = travelEndDate;
            this.viewCount = viewCount;
            this.commentCount = commentCount;
            this.nickname = nickname;
            this.memberImage = memberImage;
        }
    }
}
