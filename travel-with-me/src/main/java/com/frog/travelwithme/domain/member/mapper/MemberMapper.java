package com.frog.travelwithme.domain.member.mapper;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Follow;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/02
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    default Member toEntity(MemberDto.SignUp signUpDto) {
        Member.MemberBuilder memberBuilder = Member.builder();
        memberBuilder
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .nickname(signUpDto.getNickname())
                .gender(signUpDto.getGender())
                .nation(signUpDto.getNation())
                .address(signUpDto.getAddress())
                .role(signUpDto.getRole())
                .introduction("")
                .image("defaultImageUrl");

        return memberBuilder.build();
    }

    Member toEntity(MemberDto.Patch patchDto);

    @Mapping(target = "interests", expression = "java(member.getInterests().stream()" +
            ".map(interest -> interest.getType()).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "followerCount", expression = "java(Long.valueOf(member.getFollowings().size()))")
    @Mapping(target = "followingCount", expression = "java(Long.valueOf(member.getFollowers().size()))")
    @Mapping(target = "follow", source = "member.followings", qualifiedByName = "isFollow")
    MemberDto.Response toDto(Member member);

    @Named("isFollow")
    default boolean isFollow(List<Follow> follows) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || follows.isEmpty()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal.equals("anonymousUser")) {
            return false;
        }
        CustomUserDetails user = (CustomUserDetails) principal;
        List<String> followerEmails = follows.stream()
                .map(follow -> follow.getFollower().getEmail()).collect(Collectors.toList());

        return followerEmails.contains(user.getEmail());
    }
}
