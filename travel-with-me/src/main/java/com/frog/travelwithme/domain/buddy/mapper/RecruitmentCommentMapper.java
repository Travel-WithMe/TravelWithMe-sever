package com.frog.travelwithme.domain.buddy.mapper;

import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentUpdateDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.DeletionEntity;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentCreateDto;
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

    default RecruitmentComment toEntity(RecruitmentCommentCreateDto recruitmentCommentCreateDto) {

        if (recruitmentCommentCreateDto == null) {
            return null;
        }

        Long taggedMemberId = null;
        Long groupId = null;

        if (Optional.ofNullable(recruitmentCommentCreateDto.getTaggedMemberId()).isPresent()) {
            taggedMemberId = recruitmentCommentCreateDto.getTaggedMemberId();
        }
        if (Optional.ofNullable(recruitmentCommentCreateDto.getGroupId()).isPresent()) {
            groupId = recruitmentCommentCreateDto.getGroupId();
        }


        return RecruitmentComment.builder()
                .depth(recruitmentCommentCreateDto.getDepth())
                .groupId(groupId)
                .content(recruitmentCommentCreateDto.getContent())
                .taggedMemberId(taggedMemberId)
                .build();
    };

    default CommentDto.PostResponse toPostResponseCommentDto(RecruitmentComment comment) {

        if (comment == null) {
            return null;
        }

        Long taggedMemberId = null;
        Long groupId = null;

        if (Optional.ofNullable(comment.getTaggedMemberId()).isPresent()) {
            taggedMemberId = comment.getTaggedMemberId();
        }

        if (Optional.ofNullable(comment.getGroupId()).isPresent()) {
            groupId = comment.getGroupId();
        }

        return CommentDto.PostResponse.builder()
                .commentId(comment.getId())
                .depth(comment.getDepth())
                .groupId(groupId)
                .content(comment.getContent())
                .taggedMemberId(taggedMemberId)
                .build();
    };

    default RecruitmentCommentCreateDto postDtoToRecruitmentCommentCreateDto(CommentDto.Post postDto) {

        if (postDto == null) {
            return null;
        }

        Long taggedMemberId = null;
        Long groupId = null;

        if (Optional.ofNullable(postDto.getTaggedMemberId()).isPresent()) {
            taggedMemberId = postDto.getTaggedMemberId();
        }

        if (Optional.ofNullable(postDto.getGroupId()).isPresent()) {
            groupId = postDto.getGroupId();
        }

        return RecruitmentCommentCreateDto.builder()
                .depth(postDto.getDepth())
                .groupId(groupId)
                .content(postDto.getContent())
                .taggedMemberId(taggedMemberId)
                .build();
    }

    default RecruitmentCommentUpdateDto patchDtoToRecruitmentCommentUpdateDto(CommentDto.Patch patchDto) {

        if (patchDto == null) {
            return null;
        }

        Long taggedMemberId = null;

        if (Optional.ofNullable(patchDto.getTaggedMemberId()).isPresent()) {
            taggedMemberId = patchDto.getTaggedMemberId();
        }

        return RecruitmentCommentUpdateDto.builder()
                .content(patchDto.getContent())
                .taggedMemberId(taggedMemberId)
                .build();
    }

    default CommentDto.PatchResponse toPatchResponseCommentDto(RecruitmentComment comment) {

        if (comment == null) {
            return null;
        }

        Long taggedMemberId = null;

        if (Optional.ofNullable(comment.getTaggedMemberId()).isPresent()) {
            taggedMemberId = comment.getTaggedMemberId();
        }

        return CommentDto.PatchResponse.builder()
                .commentId(comment.getId())
                .depth(comment.getDepth())
                .groupId(comment.getGroupId())
                .content(comment.getContent())
                .taggedMemberId(taggedMemberId)
                .build();
    };

}
