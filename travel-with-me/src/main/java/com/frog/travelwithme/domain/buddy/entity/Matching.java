package com.frog.travelwithme.domain.buddy.entity;

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
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MatchingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @Builder
    public Matching(Long id, MatchingStatus status) {
        this.id = id;
        this.status = status;
    }

    public Matching(MatchingStatus status) {
        this.status = status;
    }

    public Matching request() {
        this.status = MatchingStatus.REQUEST;
        return this;
    }
    public Matching reject() {
        this.status = MatchingStatus.REJECT;
        return this;
    }
    public Matching approve() {
        this.status = MatchingStatus.APPROVE;
        return this;
    }

    public Matching cancel() {
        this.status = MatchingStatus.CANCEL;
        return this;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }

}
