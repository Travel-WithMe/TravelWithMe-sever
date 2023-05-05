package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.entity.Feed;

import java.util.List;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/02
 **/
public interface FeedCustomRepository {

    List<Feed> findAll(Long lastFeedId, String email);
}
