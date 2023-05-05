package com.frog.travelwithme.domain.feed.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

public class TagDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        String name;
        Long count;

        @Builder
        @QueryProjection
        public Response(String name, Long count) {
            this.name = name;
            this.count = count;
        }
    }
}
