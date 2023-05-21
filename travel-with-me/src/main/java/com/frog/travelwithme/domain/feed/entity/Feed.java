package com.frog.travelwithme.domain.feed.entity;

import com.frog.travelwithme.domain.common.BaseTimeEntity;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.utils.StringListConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Feed 설명: 피드 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Convert(converter = StringListConverter.class)
    private List<String> imageUrls = new ArrayList<>();

    private String contents;

    private Long commentCount = 0L;

    private Long likeCount = 0L;

    // TODO: 위치 정보 라이브러리 논의
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToMany
    @JoinTable(name = "feed_tag",
            joinColumns = @JoinColumn(name = "feed_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "feed_like",
            joinColumns = @JoinColumn(name = "feed_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> likedMembers = new ArrayList<>();

    @Builder
    public Feed(String contents, String location, Member member, List<String> imageUrls) {
        this.contents = contents;
        this.location = location;
        this.member = member;
        this.imageUrls = imageUrls;
    }

    public void updateFeedData(FeedDto.InternalPatch internalPatchDto) {
        Optional.ofNullable(internalPatchDto.getContents())
                .ifPresent(updateContents -> this.contents = updateContents);
        Optional.ofNullable(internalPatchDto.getLocation())
                .ifPresent(updateLocation -> this.location = updateLocation);
    }

    public void addTags(Set<Tag> tags) {
        this.tags.addAll(tags);
    }

    public void addLike(Member member) {
        this.likedMembers.add(member);
        this.likeCount += 1;
    }

    public void removeLike(String email) {
        this.likedMembers.stream()
                .map(Member::getEmail)
                .collect(Collectors.toList())
                .remove(email);
        this.likeCount -= 1;
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
}
