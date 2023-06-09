package com.frog.travelwithme.domain.buddy.service.dto;

import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentCommentDto {

    private Recruitment recruitment;
    private Member member;
    private Long taggedMemberId;
    private Integer depth;
    private String content;

    public RecruitmentCommentDto addRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        return this;
    }

    public RecruitmentCommentDto addMember(Member member) {
        this.member = member;
        return this;
    }
}
