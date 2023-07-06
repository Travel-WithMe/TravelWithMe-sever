package com.frog.travelwithme.domain.feed.mapper;

import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.FeedComment;
import com.frog.travelwithme.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedCommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", source = "member")
    @Mapping(target = "feed", source = "feed")
    FeedComment toEntity(CommentDto.Post postDto, Member member, Feed feed);

    @Mapping(target = "commentId", source = "id")
    CommentDto.PostResponse toPostResponseDto(FeedComment feedComment);

    @Mapping(target = "commentId", source = "id")
    CommentDto.PatchResponse toPatchResponseDto(FeedComment feedComment);
}
