package com.frog.travelwithme.domain.feed.entity;

import com.frog.travelwithme.domain.common.comment.entity.Comment;
import com.frog.travelwithme.domain.feed.service.dto.FeedCommentUpdateDto;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FeedComment 설명: 피드 댓글 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/06/28
 **/
@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedComment extends Comment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JoinTable(name = "feed_comment_like",
            joinColumns = @JoinColumn(name = "feed_comment_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> likedMembers = new ArrayList<>();

    @Builder
    public FeedComment(Long id,
                       Integer depth,
                       Long groupId,
                       Long taggedMemberId,
                       String content) {
        super(id, depth, groupId, taggedMemberId, content);
    }

    public FeedComment addMember(Member member) {
        if (member == null) {
            return null;
        }
        this.member = member;
        return this;
    }

    public FeedComment addFeed(Feed feed) {
        if (feed == null) {
            return null;
        }
        this.feed = feed;
        return this;
    }

    public FeedComment updateFeedComment(FeedCommentUpdateDto feedCommentUpdateDto) {
        Optional.ofNullable(feedCommentUpdateDto.getContent())
                .ifPresent(super::changeContent);
        Optional.ofNullable(feedCommentUpdateDto.getTaggedMemberId())
                .ifPresent(super::changeTaggedMemberId);
        return this;
    }
}
