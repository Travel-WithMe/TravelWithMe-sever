package com.frog.travelwithme.domain.common.like.service;

import com.frog.travelwithme.global.enums.EnumCollection;

public interface LikeService {

    EnumCollection.ResponseBody doLike(String email, long likedObjectId);

    EnumCollection.ResponseBody cancelLike(String email, long likedObjectId);
}
