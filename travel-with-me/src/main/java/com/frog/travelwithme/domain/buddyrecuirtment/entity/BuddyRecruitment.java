package com.frog.travelwithme.domain.buddyrecuirtment.entity;

import com.frog.travelwithme.domain.buddyrecuirtment.common.BaseTimeEntity;
import com.frog.travelwithme.domain.buddyrecuirtment.common.DeletionEntity;
import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.enums.EnumCollection.BuddyRecruitmentStatus;
import com.frog.travelwithme.global.utils.TimeUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 100, nullable = false)
    private String travelNationality;

    private LocalDateTime travelStartDate;

    private LocalDateTime travelEndDate;

    private long viewCount;

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
                            BuddyRecruitmentStatus buddyRecruitmentStatus, DeletionEntity deletionEntity) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.travelNationality = travelNationality;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.buddyRecruitmentStatus = buddyRecruitmentStatus;
        this.deletionEntity = deletionEntity;
    }

    public void addMember(Member member) {
        if(member != null) {
            this.member = member;
        }
    }

    public void updateBuddyRecruitment(BuddyDto.PatchRecruitment patchRecruitment) {
        Optional.ofNullable(patchRecruitment.getTitle())
                .ifPresent(title -> this.title = title);
        Optional.ofNullable(patchRecruitment.getContent())
                .ifPresent(content -> this.content = content);
        Optional.ofNullable(patchRecruitment.getTravelNationality())
                .ifPresent(travelNationality -> this.travelNationality = travelNationality);
        Optional.ofNullable(patchRecruitment.getTravelStartDate())
                .ifPresent(travelStartDate -> this.travelStartDate = TimeUtils.stringToLocalDateTime(travelStartDate));
        Optional.ofNullable(patchRecruitment.getTravelEndDate())
                .ifPresent(travelEndDate -> this.travelEndDate = TimeUtils.stringToLocalDateTime(travelEndDate));
    }

    public void changeStatus(BuddyRecruitmentStatus buddyRecruitmentStatus) {
        this.buddyRecruitmentStatus = buddyRecruitmentStatus;
    }

    public DeletionEntity updateDeletionEntity() {
        this.deletionEntity.setIsDeleted(true);
        this.deletionEntity.setDeletedAt(LocalDateTime.now());
        return deletionEntity;
    }

}
