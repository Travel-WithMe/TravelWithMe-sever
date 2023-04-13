package com.frog.travelwithme.domain.buddyrecuirtment.entity;

import com.frog.travelwithme.domain.buddyrecuirtment.common.BaseTimeEntity;
import com.frog.travelwithme.domain.buddyrecuirtment.common.DeletionEntity;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.enums.EnumCollection.BuddyRecruitmentStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Entity
@Getter
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuddyRecruitment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 100, nullable = false)
    private String travelNationality;

    private LocalDateTime travelStartDate;

    private LocalDateTime travelEndDate;

//    @ColumnDefault(value = "0")
    private long viewCount;

//    @ColumnDefault(value = "0")
    private long commentCount;

    @Embedded
    private DeletionEntity deletionEntity;

    @Column(length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private BuddyRecruitmentStatus buddyRecruitmentStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /*
    * TODO : 댓글과 연관관계 어떻게 지어줄지 생각하기
    * 테이블을 중간테이블로 둘 예정.
    * */

    @Builder
    public BuddyRecruitment(Long id, String title, String content, String travelNationality,
                            LocalDateTime travelStartDate, LocalDateTime travelEndDate,
                            BuddyRecruitmentStatus buddyRecruitmentStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.travelNationality = travelNationality;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.buddyRecruitmentStatus = buddyRecruitmentStatus;
    }

    public void addMember(Member member) {
        if(member == null) {
            return;
        } else {
            this.member = member;
        }
    }

}
