package com.frog.travelwithme.domain.common.comment.service;


import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentTypeDto;

public interface CommentService {
    <T> CommentTypeDto<T> createCommentTypeDto(T commentDto);
    <T> CommentDto.PostResponse createComment(CommentTypeDto<T> commentTypeDto);
}
