package com.frog.travelwithme.domain.feed.entity;

import com.frog.travelwithme.domain.common.BaseTimeEntity;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Optional;
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

    private String contents;

    private Long commentCount = 0L;

    private Long likeCount = 0L;

    // TODO: 위치 정보 라이브러리 논의
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedTag> feedTagList = new ArrayList<>();

    @Builder
    public Feed(String contents, String location, Member member, List<FeedTag> feedTagList) {
        this.contents = contents;
        this.location = location;
        this.member = member;
        this.feedTagList = feedTagList;
    }

    public void updateFeedData(FeedDto.InternalPatch internalPatchDto) {
        Optional.ofNullable(internalPatchDto.getContents())
                .ifPresent(updateContents -> this.contents = updateContents);
        Optional.ofNullable(internalPatchDto.getLocation())
                .ifPresent(updateLocation -> this.location = updateLocation);
    }

    public void addFeedTag(FeedTag feedTag) {
        if (this.feedTagList == null) {
            this.feedTagList = new ArrayList<>(List.of(feedTag));
        } else {
            List<String> tagNameList = this.feedTagList
                    .stream()
                    .map(FeedTag::getName)
                    .collect(Collectors.toList());
            if (!tagNameList.contains(feedTag.getName())) {
                this.feedTagList.add(feedTag);
            }
        }
    }
}
