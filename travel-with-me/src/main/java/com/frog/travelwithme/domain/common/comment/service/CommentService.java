package com.frog.travelwithme.domain.common.comment.service;


import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentTypeDto;

public abstract class CommentService {

    protected <T> CommentTypeDto<T> createCommentTypeDto(T commentDto) {
        CommentTypeDto<T> commentTypeDto = new CommentTypeDto<>();
        return commentTypeDto.addType(commentDto);
    }
    protected abstract <T> CommentDto.PostResponse createComment(CommentTypeDto<T> commentTypeDto);
    protected abstract <T> CommentDto.PatchResponse updateComment(CommentTypeDto<T> commentTypeDto);
}
