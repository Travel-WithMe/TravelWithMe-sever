package com.frog.travelwithme.domain.feed.mapper;

import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.FeedComment;
import com.frog.travelwithme.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedCommentMapper {

    FeedComment toEntity(CommentDto.Post postDto, Member member, Feed feed);

    CommentDto.PostResponse toPostResponseDto(FeedComment feedComment);
}
