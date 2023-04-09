package com.frog.travelwithme.domain.member.mapper;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import org.mapstruct.Mapper;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.1
 * 작성일자: 2023/04/02
 **/
@Mapper(componentModel = "spring")
public
interface MemberMapper {
    default Member toEntity(MemberDto.SignUp signUpDto) {
        Member.MemberBuilder memberBuilder = Member.builder();
        memberBuilder
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .nickname(signUpDto.getNickname())
                .nation(signUpDto.getNation())
                .address(signUpDto.getAddress())
                .image(signUpDto.getImage())
                .introduction(signUpDto.getIntroduction())
                .role(signUpDto.getRole());

        return memberBuilder.build();
    }

    Member toEntity(MemberDto.Patch patchDto);

    MemberDto.Response toDto(Member member);
}
