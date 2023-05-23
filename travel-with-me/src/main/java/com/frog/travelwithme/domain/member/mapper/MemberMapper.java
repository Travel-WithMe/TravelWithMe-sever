package com.frog.travelwithme.domain.member.mapper;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.enums.EnumCollection.Gender;
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
    default Member toEntity(MemberDto.SignUp signUpDto, String imageUrl) {
        Member.MemberBuilder memberBuilder = Member.builder();
        memberBuilder
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .nickname(signUpDto.getNickname())
                .gender(Gender.from(signUpDto.getGender()))
                .nation(signUpDto.getNation())
                .address(signUpDto.getAddress())
                .introduction(signUpDto.getIntroduction())
                .role(signUpDto.getRole())
                .image(imageUrl == null ? "defaultImage" : imageUrl);

        return memberBuilder.build();
    }

    @Mapping(target = "gender", expression = "java(com.frog.travelwithme.global.enums." +
            "EnumCollection.Gender.from(patchDto.getGender()))")
    Member toEntity(MemberDto.Patch patchDto);

    @Mapping(target = "gender", expression = "java(member.getGender().getDescription())")
    MemberDto.Response toDto(Member member);
}
