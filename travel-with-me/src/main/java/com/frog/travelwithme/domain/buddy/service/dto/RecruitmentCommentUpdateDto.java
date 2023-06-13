package com.frog.travelwithme.domain.buddy.service.dto;

import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
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
public class RecruitmentCommentUpdateDto {

    private RecruitmentComment recruitmentComment;
    private Long taggedMemberId;
    private String content;

    public RecruitmentCommentUpdateDto addRecruitmentComment(RecruitmentComment recruitmentComment) {
        this.recruitmentComment = recruitmentComment;
        return this;
    }
}
