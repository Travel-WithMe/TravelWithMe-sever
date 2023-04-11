package com.frog.travelwithme.domain.buddyrecuirtment.entity;

import com.frog.travelwithme.domain.BaseTimeEntity;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuddyRecruitment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Long viewCount;

    private Long commentCount;

    private Long travelNationality;

    private LocalDateTime travelStartDate;

    private LocalDateTime travelEndDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /*
    * TODO : 댓글과 연관관계 어떻게 지어줄지 생각하기
    * 테이블을 중간테이블로 둘 예정.
    * */
}
