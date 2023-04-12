package com.frog.travelwithme.domain.member.service;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/12
 **/
public interface MemberService {

    MemberDto.Response signUp(MemberDto.SignUp signUpDto);

    MemberDto.Response findMemberByEmail(String email);

    MemberDto.Response findMemberById(Long id);

    MemberDto.Response updateMember(MemberDto.Patch patchDto, String email);

    Member findMemberAndCheckMemberExists(Long id);

    void deleteMember(String email);
}
