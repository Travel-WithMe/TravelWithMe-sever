package com.frog.travelwithme.domain.buddy.service.dto;

import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/24
 **/

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentCommentCreateDto {

    private Recruitment recruitment;
    private Member member;
    private Integer depth;
    private Long groupId;
    private Long taggedMemberId;
    private String content;

    public RecruitmentCommentCreateDto addRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        return this;
    }

    public RecruitmentCommentCreateDto addMember(Member member) {
        this.member = member;
        return this;
    }
}
