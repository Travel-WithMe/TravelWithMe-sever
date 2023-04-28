package com.frog.travelwithme.unit.domain.recruitment.service;

import com.frog.travelwithme.domain.recruitment.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.recruitment.mapper.RecruitmentMapper;
import com.frog.travelwithme.domain.recruitment.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.recruitment.service.RecruitmentService;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.utils.TimeUtils;
import com.frog.travelwithme.utils.StubData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    @InjectMocks
    protected RecruitmentService recruitmentService;

    @Mock
    protected RecruitmentRepository recruitmentRepository;

    @Mock
    protected RecruitmentMapper buddyMapper;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("동행 모집글 작성")
    void buddyRecruitmentServiceTest1() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.Post postDto = StubData.MockRecruitment.getPostRecruitment();
        RecruitmentDto.PostResponse responseRecruitmentDto = StubData.MockRecruitment.getPostResponseRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyMapper.toEntity(postDto)).thenReturn(recruitment);
        when(recruitmentRepository.save(recruitment)).thenReturn(recruitment);
        when(buddyMapper.toPostResponseRecruitmentDto(recruitment)).thenReturn(responseRecruitmentDto);

        //when
        RecruitmentDto.PostResponse responseRecruitment = recruitmentService.createRecruitment(
                postDto, member.getEmail()
        );

        //then
        assertAll(
                () -> assertEquals(responseRecruitment.getTitle(), recruitment.getTitle()),
                () -> assertEquals(responseRecruitment.getContent(), recruitment.getContent()),
                () -> assertEquals(responseRecruitment.getTravelNationality(), recruitment.getTravelNationality()),
                () -> assertEquals(responseRecruitment.getNickname(), recruitment.getMember().getNickname()),
                () -> assertEquals(responseRecruitment.getMemberImage(), recruitment.getMember().getImage())
        );
    }

    @Test
    @DisplayName("동행 모집글 수정")
    void buddyRecruitmentServiceTest2() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.Patch patchDto = StubData.MockRecruitment.getPatchRecruitment();
        RecruitmentDto.PatchResponse responseRecruitmentDto = StubData.MockRecruitment.getPatchResponseRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));
        when(buddyMapper.toPatchResponseRecruitmentDto(recruitment)).thenReturn(responseRecruitmentDto);

        //when
        RecruitmentDto.PatchResponse responseRecruitment = recruitmentService.updateRecruitment(
                patchDto, recruitment.getId(), member.getEmail()
        );

        //then
        assertAll(
                () -> assertEquals(responseRecruitment.getTitle(), recruitment.getTitle()),
                () -> assertEquals(responseRecruitment.getContent(), recruitment.getContent()),
                () -> assertEquals(responseRecruitment.getTravelNationality(), recruitment.getTravelNationality()),
                () -> assertEquals(responseRecruitment.getTravelStartDate(), TimeUtils.localDateTimeToLocalDate(recruitment.getTravelStartDate())),
                () -> assertEquals(responseRecruitment.getTravelEndDate(), TimeUtils.localDateTimeToLocalDate(recruitment.getTravelEndDate()))
        );
    }

    @Test
    @DisplayName("동행 모집글 수정 - 작성자, 유저 불일치")
    void buddyRecruitmentServiceTest3() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.Patch patchDto = StubData.MockRecruitment.getPatchRecruitment();
        RecruitmentDto.PatchResponse responseRecruitmentDto = StubData.MockRecruitment.getPatchResponseRecruitment();
        Member writer = StubData.MockMember.getMemberByEmailAndNickname("dhfif718@naver.com", "LJH");
        Member user = StubData.MockMember.getMemberByEmailAndNickname("kkd718@gmail.com", "KCB");
        recruitment.addMember(writer);

        when(memberService.findMemberAndCheckMemberExists(user.getEmail())).thenReturn(user);
        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.updateRecruitment(
                        patchDto,
                        recruitment.getId(),
                        user.getEmail()
                )
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 모집글 삭제")
    void buddyRecruitmentServiceTest4() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));

        //when
        recruitmentService.deleteRecruitment(recruitment.getId(), member.getEmail());

        //then

    }

    @Test
    @DisplayName("동행 모집글 삭제 - 작성자, 유저 불일치")
    void buddyRecruitmentServiceTest5() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMemberByEmailAndNickname("dhfif718@naver.com", "LJH");
        Member user = StubData.MockMember.getMemberByEmailAndNickname("kkd718@gmail.com", "KCB");
        recruitment.addMember(writer);

        when(memberService.findMemberAndCheckMemberExists(user.getEmail())).thenReturn(user);
        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.deleteRecruitment(recruitment.getId(), user.getEmail())
        ).isInstanceOf(BusinessLogicException.class);
    }
}
