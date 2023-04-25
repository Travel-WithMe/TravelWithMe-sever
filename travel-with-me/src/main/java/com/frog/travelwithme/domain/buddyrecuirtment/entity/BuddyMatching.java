package com.frog.travelwithme.domain.buddyrecuirtment.entity;

import com.frog.travelwithme.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

import static com.frog.travelwithme.global.enums.EnumCollection.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuddyMatching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private BuddyMatchingStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "buddy_recruitment_id")
    private BuddyRecruitment buddyRecruitment;

    @Builder
    public BuddyMatching(BuddyMatchingStatus status) {
        this.status = status;
    }

    public void changeStatus(BuddyMatchingStatus status) {
        this.status = status;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addBuddyRecruitment(BuddyRecruitment buddyRecruitment) {
        this.buddyRecruitment = buddyRecruitment;
    }

}
