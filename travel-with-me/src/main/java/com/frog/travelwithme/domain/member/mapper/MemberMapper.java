package com.frog.travelwithme.domain.member.mapper;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.entity.MemberDto;
import org.mapstruct.Mapper;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/02
 **/
@Mapper(componentModel = "spring")
public
interface MemberMapper {
    default Member toEntity(MemberDto.SignUp signUpDto) {
        Member.MemberBuilder memberBuilder = Member.builder();
        memberBuilder.signUpDto(signUpDto);

        return memberBuilder.build();
    }

    MemberDto.SingUpResponse toDto(Member member);
}
