package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.entity.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/07/03
 **/
public interface FeedCommentRepository
        extends JpaRepository<FeedComment, Long>, FeedCommentCustomRepository {
}
