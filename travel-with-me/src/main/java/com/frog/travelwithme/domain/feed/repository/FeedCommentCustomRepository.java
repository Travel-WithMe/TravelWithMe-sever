package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.entity.FeedComment;

import java.util.List;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/07/10
 **/
public interface FeedCommentCustomRepository {

    List<FeedComment> findAllByFeedId(Long feedId, String email, Long lastCommentId, int size);
}
