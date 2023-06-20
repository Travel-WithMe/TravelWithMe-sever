package com.frog.travelwithme.domain.common.like.service;

public interface LikeService {

    void doLike(String email, long likedObjectId);

    void cancelLike(String email, long likedObjectId);
}
