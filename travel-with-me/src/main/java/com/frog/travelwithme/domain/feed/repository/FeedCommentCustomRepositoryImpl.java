package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.entity.FeedComment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.frog.travelwithme.domain.feed.entity.QFeedComment.feedComment;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/07/10
 **/
@Repository
@RequiredArgsConstructor
public class FeedCommentCustomRepositoryImpl implements FeedCommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FeedComment> findAllByFeedId(Long feedId, String email, Long lastCommentId, int size) {
        return jpaQueryFactory
                .selectFrom(feedComment)
                .where(ltCommentId(lastCommentId))
                .where(feedComment.feed.id.eq(feedId))
                .orderBy(feedComment.groupId.asc(), feedComment.depth.asc(), feedComment.id.asc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression ltCommentId(Long lastCommentId) {
        if (lastCommentId == null) {
            return null;
        }
        return feedComment.id.gt(lastCommentId);
    }
}
