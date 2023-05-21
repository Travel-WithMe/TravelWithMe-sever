package com.frog.travelwithme.domain.feed.mapper;

import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.Tag;
import com.frog.travelwithme.domain.member.entity.Member;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    @Mapping(target = "tags", source = "tags", qualifiedByName = "convertTagNamesFromTags")
    FeedDto.ResponseDetail toResponseDetail(Feed feed);

    @Mapping(target = "profileImage", source = "feed.member.image")
    @Mapping(target = "tags", source = "feed.tags", qualifiedByName = "convertTagNamesFromTags")
    @Mapping(target = "writer", expression = "java(feed.getMember().getEmail().equals(email))")
    @Mapping(target = "liked", expression = "java(!feed.getLikedMembers().isEmpty() && " +
            "feed.getLikedMembers().stream().map(Member::getEmail)" +
            ".collect(java.util.stream.Collectors.toList()).contains(email))")
    @Mapping(target = "nickname", expression = "java(feed.getMember().getNickname())")
    FeedDto.Response toResponse(Feed feed, String email);

    default List<FeedDto.Response> toResponseList(List<Feed> feedList, String email) {
        return feedList.stream().map(feed -> toResponse(feed, email)).collect(Collectors.toList());
    }

    @Named("convertTagNamesFromTags")
    default List<String> convertTagNamesFromTags(Set<Tag> tags) {
        if (tags != null) {
            return tags.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
