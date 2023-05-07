package com.frog.travelwithme.domain.feed.mapper;

import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.FeedTag;
import com.frog.travelwithme.domain.member.entity.Member;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/30
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedMapper {

    Feed postDtoToFeed(FeedDto.Post postDto, Member member);

    FeedDto.InternalPatch toInternalDto(FeedDto.Patch patchDto);

    FeedDto.ResponseDetail toResponseDetail(Feed feed);

    @Mapping(target = "profileImage", source = "feed.member.image")
    @Mapping(target = "tags", source = "feed.feedTagList", qualifiedByName = "feedTagListToTagNames")
    @Mapping(target = "writer", expression = "java(feed.getMember().getEmail().equals(email))")
    @Mapping(target = "nickname", expression = "java(feed.getMember().getNickname())")
    FeedDto.Response toResponse(Feed feed, String email);

    List<FeedDto.Response> toResponseList(List<Feed> feedList);

    @Named("feedTagListToTagNames")
    default List<String> feedTagListToTagNames(List<FeedTag> feedTagList) {
        if (feedTagList != null) {
            return feedTagList.stream()
                    .map(FeedTag::getName)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
