package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
public interface FeedRepository extends JpaRepository<Feed, Long>, FeedCustomRepository {
    Optional<Feed> findById(long feedId);

}
