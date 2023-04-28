package com.frog.travelwithme.domain.buddy.entity;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
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
public class Buddy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private BuddyStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @Builder
    public Buddy(BuddyStatus status) {
        this.status = status;
    }

    public void changeWait() {
        this.status = BuddyStatus.WAIT;
    }
    public void changeReject() {
        this.status = BuddyStatus.REJECT;
    }
    public void changeApprove() {
        this.status = BuddyStatus.APPROVE;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }

}
