package com.frog.travelwithme.unit.domain.member.service;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.mapper.MemberMapper;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.member.service.MemberServiceImpl;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.utils.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberMapper memberMapper;

    @Test
    @DisplayName("회원가입")
    void memberServiceTest1() {
        // given
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberMapper.toEntity(any(MemberDto.SignUp.class))).willReturn(member);
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberMapper.toDto(any(Member.class))).willReturn(expectedResponse);

        // when
        MemberDto.Response response = memberServiceImpl.signUp(signUpDto);

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
        MemberDto.Response response = memberServiceImpl.findMemberByEmail(email);

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
        MemberDto.Response response = memberServiceImpl.findMemberById(id);

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
        given(memberMapper.toDto(any(Member.class))).willReturn(expectedResponse);

        // when
        MemberDto.Response response = memberServiceImpl.updateMember(patchDto, email);

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
        Member member = StubData.MockMember.getMember();
        MemberDto.Response expectedResponse = StubData.MockMember.getResponseDto();
        given(memberMapper.toEntity(any(MemberDto.SignUp.class))).willReturn(member);
        given(memberRepository.findByEmail(any())).willReturn(Optional.ofNullable(member));

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberServiceImpl.signUp(signUpDto));
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 시 예외 발생(email)")
    void memberServiceTest6() {
        // given
        String email = "email";
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberServiceImpl.findMemberByEmail(email));
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 시 예외 발생(id)")
    void memberServiceTest7() {
        // given
        Long id = 1L;
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberServiceImpl.findMemberById(id));
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
                () -> memberServiceImpl.updateMember(patchDto, email));
    }

    @Test
    @DisplayName("존재하지 않는 회원 삭제 시 예외 발생")
    void memberServiceTest9() {
        // given
        String email = "email";
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when // then
        assertThrows(BusinessLogicException.class,
                () -> memberServiceImpl.deleteMember(email));
    }
}
