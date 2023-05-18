package com.frog.travelwithme.domain.buddy.entity;

import com.frog.travelwithme.domain.common.BaseTimeEntity;
import com.frog.travelwithme.domain.common.DeletionEntity;
import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.enums.EnumCollection.RecruitmentStatus;
import com.frog.travelwithme.global.utils.TimeUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class Recruitment extends BaseTimeEntity {

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
    RecruitmentStatus recruitmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recruitment", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Matching> matchingList = new ArrayList<>();

    /*
    * TODO : 댓글과 연관관계 어떻게 지어줄지 생각하기
    * 테이블을 중간테이블로 둘 예정.
    * */

    @Builder
    public Recruitment(Long id, String title, String content, String travelNationality,
                       LocalDateTime travelStartDate, LocalDateTime travelEndDate,
                       RecruitmentStatus recruitmentStatus, DeletionEntity deletionEntity) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.travelNationality = travelNationality;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.deletionEntity = deletionEntity;
    }

    public Recruitment addMember(Member member) {
        if(member != null) {
            this.member = member;
        }
        return this;
    }

    public Recruitment addMatching(Matching matching) {
        if(matching != null) {
            this.matchingList.add(matching);
        }
        return this;
    }

    public Recruitment updateBuddyRecruitment(RecruitmentDto.Patch patch) {
        Optional.ofNullable(patch.getTitle())
                .ifPresent(title -> this.title = title);
        Optional.ofNullable(patch.getContent())
                .ifPresent(content -> this.content = content);
        Optional.ofNullable(patch.getTravelNationality())
                .ifPresent(travelNationality -> this.travelNationality = travelNationality);
        Optional.ofNullable(patch.getTravelStartDate())
                .ifPresent(travelStartDate -> this.travelStartDate = TimeUtils.stringToLocalDateTime(travelStartDate));
        Optional.ofNullable(patch.getTravelEndDate())
                .ifPresent(travelEndDate -> this.travelEndDate = TimeUtils.stringToLocalDateTime(travelEndDate));
        return this;
    }

    public void inProgress() {
        this.recruitmentStatus = RecruitmentStatus.IN_PROGRESS;
    }

    public void end() {
        this.recruitmentStatus = RecruitmentStatus.END;
    }

    public void updateDeletionEntity() {
        this.deletionEntity.setIsDeleted(true);
        this.deletionEntity.setDeletedAt(LocalDateTime.now());
    }

}
