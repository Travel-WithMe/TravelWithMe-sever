package com.frog.travelwithme.domain.feed.service.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCommentUpdateDto {
    private Long taggedMemberId;
    private String content;
}
