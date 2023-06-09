package com.frog.travelwithme.unit.domain.member.service;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.mapper.MemberMapper;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.member.service.InterestService;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.enums.EnumCollection.AwsS3Path;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.file.FileUploadService;
import com.frog.travelwithme.utils.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private InterestService interestService;

    @Test
    @DisplayName("회원가입")
    void memberServiceTest1() {
        // given
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(interestService.findInterests(anyList())).willReturn(new ArrayList<>());
        given(memberMapper.toEntity(any(MemberDto.SignUp.class))).willReturn(member);
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberMapper.toDto(any(Member.class))).willReturn(expectedResponse);

        // when
        MemberDto.Response response = memberService.signUp(signUpDto);

        // then
        assertNotNull(response);
        assertThat(response.getEmail()).isEqualTo(expectedResponse.getEmail());
        assertThat(response.getNickname()).isEqualTo(expectedResponse.getNickname());
        assertThat(response.getNation()).isEqualTo(expectedResponse.getNation());
        assertThat(response.getAddress()).isEqualTo(expectedResponse.getAddress());
        assertThat(response.getIntroduction()).isEqualTo(expectedResponse.getIntroduction());
        assertThat(response.getRole()).isEqualTo(expectedResponse.getRole());
    }

    @Test
    @DisplayName("Email로 회원 조회")
    void memberServiceTest2() {
        // given
        String email = "email";
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberRepository.findByEmail(any())).willReturn(Optional.ofNullable(member));
        given(memberMapper.toDto(any(Member.class))).willReturn(expectedResponse);

        // when
        MemberDto.Response response = memberService.findMemberByEmail(email);

        // then
        assertNotNull(response);
        assertThat(response.getEmail()).isEqualTo(expectedResponse.getEmail());
        assertThat(response.getNickname()).isEqualTo(expectedResponse.getNickname());
        assertThat(response.getNation()).isEqualTo(expectedResponse.getNation());
        assertThat(response.getAddress()).isEqualTo(expectedResponse.getAddress());
        assertThat(response.getIntroduction()).isEqualTo(expectedResponse.getIntroduction());
        assertThat(response.getRole()).isEqualTo(expectedResponse.getRole());
    }

    @Test
    @DisplayName("Id로 회원 조회")
    void memberServiceTest3() {
        // given
        Long id = 1L;
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(member));
        given(memberMapper.toDto(any(Member.class))).willReturn(expectedResponse);

        // when
        MemberDto.Response response = memberService.findMemberById(id);

        // then
        assertNotNull(response);
        assertThat(response.getEmail()).isEqualTo(expectedResponse.getEmail());
        assertThat(response.getNickname()).isEqualTo(expectedResponse.getNickname());
        assertThat(response.getNation()).isEqualTo(expectedResponse.getNation());
        assertThat(response.getAddress()).isEqualTo(expectedResponse.getAddress());
        assertThat(response.getIntroduction()).isEqualTo(expectedResponse.getIntroduction());
        assertThat(response.getRole()).isEqualTo(expectedResponse.getRole());
    }

    @Test
    @DisplayName("회원 정보 수정")
    void memberServiceTest4() {
        // given
        String email = "email";
        MemberDto.Patch patchDto = StubData.MockMember.getPatchDto();
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberRepository.findByEmail(any())).willReturn(Optional.ofNullable(member));
        given(interestService.findInterests(anyList())).willReturn(new ArrayList<>());
        given(memberMapper.toDto(any(Member.class))).willReturn(expectedResponse);

        // when
        MemberDto.Response response = memberService.updateMember(patchDto, email);

        // then
        assertNotNull(response);
        assertThat(response.getEmail()).isEqualTo(expectedResponse.getEmail());
        assertThat(response.getNickname()).isEqualTo(expectedResponse.getNickname());
        assertThat(response.getNation()).isEqualTo(expectedResponse.getNation());
        assertThat(response.getAddress()).isEqualTo(expectedResponse.getAddress());
        assertThat(response.getIntroduction()).isEqualTo(expectedResponse.getIntroduction());
        assertThat(response.getRole()).isEqualTo(expectedResponse.getRole());
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 회원이면 예외 발생")
    void memberServiceTest5() {
        // given
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        MultipartFile file = StubData.CustomMultipartFile.getMultipartFile();
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberRepository.findByEmail(any())).willReturn(Optional.ofNullable(member));

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberService.signUp(signUpDto));
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 시 예외 발생(email)")
    void memberServiceTest6() {
        // given
        String email = "email";
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberService.findMemberByEmail(email));
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 시 예외 발생(id)")
    void memberServiceTest7() {
        // given
        Long id = 1L;
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberService.findMemberById(id));
    }

    @Test
    @DisplayName("존재하지 않는 회원 수정 시 예외 발생")
    void memberServiceTest8() {
        // given
        String email = "email";
        MemberDto.Patch patchDto = StubData.MockMember.getPatchDto();
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberService.updateMember(patchDto, email));
    }

    @Test
    @DisplayName("존재하지 않는 회원 삭제 시 예외 발생")
    void memberServiceTest9() {
        // given
        String email = "email";
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberService.deleteMember(email));
    }

    @Test
    @DisplayName("프로필 이미지 변경")
    void memberServiceTest10() {
        // given
        MockMultipartFile file = StubData.CustomMockMultipartFile.getFile();
        String email = "email";
        Member originMember = StubData.MockMember.getMember();
        originMember.changeImage(StubData.MockMember.getImage());
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(originMember));
        given(memberMapper.toDto(any(Member.class))).willReturn(expectedResponse);
        given(fileUploadService.upload(any(MultipartFile.class), any(AwsS3Path.class))).willReturn("imageUrl");

        // when
        MemberDto.Response response = memberService.changeProfileImage(file, email);

        // then
        assertNotNull(response);
        assertThat(response.getImage()).isEqualTo(expectedResponse.getImage());
        assertThat(response.getEmail()).isEqualTo(expectedResponse.getEmail());
        assertThat(response.getNickname()).isEqualTo(expectedResponse.getNickname());
        assertThat(response.getNation()).isEqualTo(expectedResponse.getNation());
        assertThat(response.getAddress()).isEqualTo(expectedResponse.getAddress());
        assertThat(response.getIntroduction()).isEqualTo(expectedResponse.getIntroduction());
        assertThat(response.getRole()).isEqualTo(expectedResponse.getRole());
    }
}
