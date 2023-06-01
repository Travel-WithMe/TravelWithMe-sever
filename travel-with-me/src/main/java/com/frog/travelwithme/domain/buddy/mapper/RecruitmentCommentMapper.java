package com.frog.travelwithme.domain.buddy.mapper;

import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.DeletionEntity;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/24
 **/

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecruitmentCommentMapper {

    default RecruitmentComment toEntity(RecruitmentCommentDto recruitmentCommentDto) {

        if (recruitmentCommentDto == null) {
            return null;
        }

        Long taggedMemberId = null;

        if (Optional.ofNullable(recruitmentCommentDto.getTaggedMemberId()).isPresent()) {
            taggedMemberId = recruitmentCommentDto.getTaggedMemberId();
        }

        DeletionEntity deletionEntity = new DeletionEntity();

        return RecruitmentComment.builder()
                .taggedMemberId(taggedMemberId)
                .depth(recruitmentCommentDto.getDepth())
                .content(recruitmentCommentDto.getContent())
                .deletionEntity(deletionEntity)
                .build();
    };

    default CommentDto.PostResponse toPostResponseCommentDto(RecruitmentComment comment) {

        if (comment == null) {
            return null;
        }

        Long taggedMemberId = null;

        if (Optional.ofNullable(comment.getTaggedMemberId()).isPresent()) {
            taggedMemberId = comment.getTaggedMemberId();
        }

        return CommentDto.PostResponse.builder()
                .commentId(comment.getId())
                .depth(comment.getDepth())
                .content(comment.getContent())
                .taggedMemberId(taggedMemberId)
                .build();
    };

    default RecruitmentCommentDto createRecruitmentCommentDto(CommentDto.Post postDto) {

        if (postDto == null) {
            return null;
        }

        Long taggedMemberId = null;

        if (Optional.ofNullable(postDto.getTaggedMemberId()).isPresent()) {
            taggedMemberId = postDto.getTaggedMemberId();
        }

        return RecruitmentCommentDto.builder()
                .depth(postDto.getDepth())
                .content(postDto.getContent())
                .taggedMemberId(taggedMemberId)
                .build();
    }

}
