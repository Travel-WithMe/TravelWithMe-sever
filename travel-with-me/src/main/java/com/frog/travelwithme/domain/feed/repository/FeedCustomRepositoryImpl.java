package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.entity.Feed;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.frog.travelwithme.domain.feed.entity.QFeed.feed;
import static com.frog.travelwithme.domain.feed.entity.QFeedTag.feedTag;
import static com.frog.travelwithme.domain.member.entity.QMember.member;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/02
 **/
@Repository
@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Feed> findAll(Long lastFeedId, String email) {
        int pageSize = 20;

        return jpaQueryFactory
                .selectFrom(feed)
                .leftJoin(feed.member, member)
                .leftJoin(feed.feedTagList, feedTag)
                .where(ltFeedId(lastFeedId))
                .orderBy(feed.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression ltFeedId(Long lastFeedId) {
        if (lastFeedId == null) {
            return null;
        }
        return feed.id.lt(lastFeedId);
    }
}
