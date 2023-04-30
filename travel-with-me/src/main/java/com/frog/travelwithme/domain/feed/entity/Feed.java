package com.frog.travelwithme.domain.feed.entity;

import com.frog.travelwithme.domain.buddyrecuirtment.common.BaseTimeEntity;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String content;

    private Long commentCount = 0L;

    private Long likeCount = 0L;

    // TODO: 위치 정보 라이브러리 논의
    private String locate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "feedTag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedTag> feedTagList = new ArrayList<>();

    @Builder
    public Feed(String content, Long commentCount, Long likeCount,
                String locate, Member member, List<FeedTag> feedTagList) {
        this.content = content;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.locate = locate;
        this.member = member;
        this.feedTagList = feedTagList;
    }
}
