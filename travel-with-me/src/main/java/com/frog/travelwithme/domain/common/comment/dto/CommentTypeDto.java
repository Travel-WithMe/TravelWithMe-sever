package com.frog.travelwithme.domain.common.comment.dto;

import lombok.Getter;

@Getter
public class CommentTypeDto<T> {

    private T type;

    public CommentTypeDto<T> addType(T type) {
        this.type = type;
        return this;
    }
}
