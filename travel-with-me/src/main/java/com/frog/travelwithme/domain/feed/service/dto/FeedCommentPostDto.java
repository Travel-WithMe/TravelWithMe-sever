package com.frog.travelwithme.domain.feed.service.dto;

import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCommentPostDto {
    private Feed feed;
    private Member member;
    private Integer depth;
    private Long groupId;
    private Long taggedMemberId;
    private String content;

    public FeedCommentPostDto addFeed(Feed feed) {
        this.feed = feed;
        return this;
    }

    public FeedCommentPostDto addMember(Member member) {
        this.member = member;
        return this;
    }
}
