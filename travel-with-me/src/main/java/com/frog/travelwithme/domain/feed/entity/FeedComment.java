package com.frog.travelwithme.domain.feed.entity;

import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.comment.entity.Comment;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @ManyToMany
    @JoinTable(name = "feed_comment_like",
            joinColumns = @JoinColumn(name = "feed_comment_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> likedMembers = new ArrayList<>();

    @Builder
    public FeedComment(Long id,
                       Integer depth,
                       Long groupId,
                       Long taggedMemberId,
                       String content,
                       Member member,
                       Feed feed) {
        super(id, depth, groupId, taggedMemberId, content);
        this.member = member;
        this.feed = feed;
    }

    public void updateFeedComment(CommentDto.Patch patchDto) {
        Optional.ofNullable(patchDto.getContent())
                .ifPresent(super::changeContent);
        super.changeTaggedMemberId(patchDto.getTaggedMemberId());
    }

    public boolean isLikedByMember(String email) {
        if (this.likedMembers.isEmpty()) {
            return false;
        }
        return this.likedMembers.stream()
                .map(Member::getEmail)
                .collect(Collectors.toList())
                .contains(email);
    }

    public void addLike(Member member) {
        this.likedMembers.add(member);
        super.addLikeCount();
    }

    public void removeLike(String email) {
        this.likedMembers.stream()
                .map(Member::getEmail)
                .collect(Collectors.toList())
                .remove(email);
        super.minusLikeCount();
    }
}
