package com.frog.travelwithme.domain.common.comment.service;


import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentTypeDto;
import com.frog.travelwithme.domain.common.comment.entity.Comment;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/24
 **/

@Slf4j
@RequiredArgsConstructor
public abstract class CommentService {

    private final MemberService memberService;

    protected abstract <T> CommentDto.PostResponse createComment(CommentTypeDto<T> commentTypeDto);

    protected abstract <T> CommentDto.PatchResponse updateComment(CommentTypeDto<T> commentTypeDto);

    protected abstract void checkExistCommentById(Long id);

    protected Comment joinGroup(Comment comment) {
        if(this.isComment(comment.getDepth())) {
            comment.addGroupId(comment.getId());
        }
        return comment;
    }

    protected <T> CommentTypeDto<T> createCommentTypeDto(T commentDto) {
        CommentTypeDto<T> commentTypeDto = new CommentTypeDto<>();
        return commentTypeDto.addType(commentDto);
    }

    protected void checkPossibleToMakeGroup(CommentDto.Post postDto) {
        Integer depth = postDto.getDepth();
        if (this.isComment(depth)) {
            this.checkAvailableComment(postDto);
        } else if (this.isReply(depth)) {
            this.checkAvailableReply(postDto);
        }
    }

    public void checkExistTaggedMemberId(CommentDto.Post postDto) {
        if (this.hasTaggedMemberId(postDto)) {
            try {
                memberService.findMember(postDto.getTaggedMemberId());
            } catch (BusinessLogicException e) {
                log.debug("CommentService.checkExistTaggedMemberId exception occur postDto: {}", postDto);
                throw new BusinessLogicException(ExceptionCode.TAGGED_MEMBER_NOT_FOUND);
            }
        }
    }

    private void checkAvailableComment(CommentDto.Post postDto) {
        if (this.hasGroupId(postDto)) {
            log.debug("CommentService.checkAvailableComment exception occur " +
                    "postDto: {}", postDto);
            throw new BusinessLogicException(ExceptionCode.COMMENT_DO_NOT_NEED_GROUP_ID);
        }
    }

    private void checkAvailableReply(CommentDto.Post postDto) {
        if (this.hasGroupId(postDto)) {
            try {
                this.checkExistCommentById(postDto.getGroupId());
            } catch (BusinessLogicException e) {
                log.debug("CommentService.checkAvailableReply hasGroupId exception occur postDto: {}", postDto);
                throw new BusinessLogicException(ExceptionCode.COMMENT_REPLY_GROUP_ID_NOT_FOUND);
            }
        } else {
            log.debug("CommentService.checkAvailableReply exception occur postDto: {}", postDto);
            throw new BusinessLogicException(ExceptionCode.COMMENT_REPLY_NEED_GROUP_ID);
        }
    }

    private boolean isComment(Integer depth) {
        return depth == 1;
    }

    private boolean isReply(Integer depth) {
        return depth == 2;
    }

    private boolean hasGroupId(CommentDto.Post postDto) {
        return Optional.ofNullable(postDto.getGroupId()).isPresent();
    }

    private boolean hasTaggedMemberId(CommentDto.Post postDto) {
        return Optional.ofNullable(postDto.getTaggedMemberId()).isPresent();
    }
}
