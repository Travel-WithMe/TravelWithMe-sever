package com.frog.travelwithme.domain.feed.mapper;

import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.feed.entity.FeedComment;
import com.frog.travelwithme.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedCommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", source = "member")
    @Mapping(target = "feed", source = "feed")
    FeedComment toEntity(CommentDto.Post postDto, Member member, Feed feed);

    @Mapping(target = "commentId", source = "feedComment.id")
    @Mapping(target = "writer", expression = "java(feedComment.getMember().getEmail().equals(email))")
    @Mapping(target = "liked", expression = "java(!feedComment.getLikedMembers().isEmpty() && " +
            "feedComment.getLikedMembers().stream().map(Member::getEmail)" +
            ".collect(java.util.stream.Collectors.toList()).contains(email))")
    CommentDto.PostResponse toPostResponseDto(FeedComment feedComment, String email);

    @Mapping(target = "commentId", source = "feedComment.id")
    @Mapping(target = "taggedMemberNickname", source = "nickname")
    @Mapping(target = "writer", expression = "java(feedComment.getMember().getEmail().equals(email))")
    @Mapping(target = "liked", expression = "java(!feedComment.getLikedMembers().isEmpty() && " +
            "feedComment.getLikedMembers().stream().map(Member::getEmail)" +
            ".collect(java.util.stream.Collectors.toList()).contains(email))")
    CommentDto.PostResponse toPostResponseDto(FeedComment feedComment, String nickname, String email);

    @Mapping(target = "commentId", source = "feedComment.id")
    @Mapping(target = "writer", expression = "java(feedComment.getMember().getEmail().equals(email))")
    @Mapping(target = "liked", expression = "java(!feedComment.getLikedMembers().isEmpty() && " +
            "feedComment.getLikedMembers().stream().map(Member::getEmail)" +
            ".collect(java.util.stream.Collectors.toList()).contains(email))")
    CommentDto.PatchResponse toPatchResponseDto(FeedComment feedComment, String email);

    @Mapping(target = "commentId", source = "feedComment.id")
    @Mapping(target = "taggedMemberNickname", source = "nickname")
    @Mapping(target = "writer", expression = "java(feedComment.getMember().getEmail().equals(email))")
    @Mapping(target = "liked", expression = "java(!feedComment.getLikedMembers().isEmpty() && " +
            "feedComment.getLikedMembers().stream().map(Member::getEmail)" +
            ".collect(java.util.stream.Collectors.toList()).contains(email))")
    CommentDto.PatchResponse toPatchResponseDto(FeedComment feedComment, String nickname, String email);

    @Mapping(target = "commentId", source = "feedComment.id")
    @Mapping(target = "content", source = "deleteContent")
    @Mapping(target = "writer", expression = "java(feedComment.getMember().getEmail().equals(email))")
    @Mapping(target = "liked", expression = "java(!feedComment.getLikedMembers().isEmpty() && " +
            "feedComment.getLikedMembers().stream().map(Member::getEmail)" +
            ".collect(java.util.stream.Collectors.toList()).contains(email))")
    CommentDto.DeleteResponse toDelteResponseDto(FeedComment feedComment, String deleteContent, String email);

    @Mapping(target = "commentId", source = "feedComment.id")
    @Mapping(target = "writer", expression = "java(feedComment.getMember().getEmail().equals(email))")
    @Mapping(target = "liked", expression = "java(!feedComment.getLikedMembers().isEmpty() && " +
            "feedComment.getLikedMembers().stream().map(Member::getEmail)" +
            ".collect(java.util.stream.Collectors.toList()).contains(email))")
    CommentDto.GetResponse toGetResponseDto(FeedComment feedComment, String email);

    default List<CommentDto.GetResponse> toGetResponseDtoList(List<FeedComment> feedComments, String email) {
        if (feedComments == null) {
            return null;
        }

        return feedComments.stream()
                .map(feedComment -> toGetResponseDto(feedComment, email))
                .collect(Collectors.toList());
    }
}
