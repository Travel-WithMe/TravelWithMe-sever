package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.controller.dto.QTagDto_Response;
import com.frog.travelwithme.domain.feed.controller.dto.TagDto;
import com.frog.travelwithme.domain.feed.entity.QTag;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.frog.travelwithme.domain.feed.entity.QFeed.feed;
import static com.frog.travelwithme.domain.feed.entity.QTag.tag;
import static com.querydsl.core.types.ExpressionUtils.count;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/03
 **/
@Repository
@RequiredArgsConstructor
public class TagCustomRepositoryImpl implements TagCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TagDto.Response> findTagsStartWith(String tagName, int size) {
        QTag subTag = new QTag("subTag");
        return jpaQueryFactory.select(
                        new QTagDto_Response(
                                tag.name,
                                Expressions.as(
                                        JPAExpressions.select(count(subTag))
                                                .from(subTag)
                                                .where(subTag.eq(tag))
                                                .join(tag.feeds, feed)
                                                .groupBy(subTag.id),
                                        "count"
                                )))
                .from(tag)
                .where(tag.name.startsWith(tagName))
                .limit(size)
                .orderBy()
                .fetch();
    }
}
