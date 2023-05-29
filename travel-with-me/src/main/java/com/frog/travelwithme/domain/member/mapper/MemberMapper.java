package com.frog.travelwithme.domain.member.mapper;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

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
                .introduction(signUpDto.getIntroduction())
                .role(signUpDto.getRole())
                .image("defaultImageUrl");

        return memberBuilder.build();
    }

    Member toEntity(MemberDto.Patch patchDto);

    @Mapping(target = "interests", expression = "java(member.getInterests().stream()" +
            ".map(interest -> interest.getType()).collect(java.util.stream.Collectors.toList()))")
    MemberDto.Response toDto(Member member);
}
