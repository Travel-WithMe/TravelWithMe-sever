package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.mapper.BuddyMapper;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentService;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.utils.TimeUtils;
import com.frog.travelwithme.utils.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/


@ExtendWith(MockitoExtension.class)
class BuddyRecruitmentServiceTest {

    @InjectMocks
    protected BuddyRecruitmentService buddyRecruitmentService;

    @Mock
    protected BuddyRecruitmentRepository buddyRecruitmentRepository;

    @Mock
    protected BuddyMapper buddyMapper;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("동행 모집글 작성")
    void buddyRecruitmentServiceTest1() {
        //given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        BuddyDto.PostRecruitment postRecruitmentDto = StubData.MockBuddy.getPostRecruitment();
        BuddyDto.PostResponseRecruitment responseRecruitmentDto = StubData.MockBuddy.getPostResponseRecruitment();
        Member member = StubData.MockMember.getMember();
        buddyRecruitment.addMember(member);

        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyMapper.toEntity(postRecruitmentDto)).thenReturn(buddyRecruitment);
        when(buddyRecruitmentRepository.save(buddyRecruitment)).thenReturn(buddyRecruitment);
        when(buddyMapper.toPostResponseRecruitmentDto(buddyRecruitment)).thenReturn(responseRecruitmentDto);

        //when
        BuddyDto.PostResponseRecruitment responseRecruitment = buddyRecruitmentService.createBuddyRecruitment(
                postRecruitmentDto, member.getEmail()
        );

        //then
        assertAll(
                () -> assertEquals(responseRecruitment.getTitle(), buddyRecruitment.getTitle()),
                () -> assertEquals(responseRecruitment.getContent(), buddyRecruitment.getContent()),
                () -> assertEquals(responseRecruitment.getTravelNationality(), buddyRecruitment.getTravelNationality()),
                () -> assertEquals(responseRecruitment.getNickname(), buddyRecruitment.getMember().getNickname()),
                () -> assertEquals(responseRecruitment.getMemberImage(), buddyRecruitment.getMember().getImage())
        );
    }

    @Test
    @DisplayName("동행 모집글 수정")
    void buddyRecruitmentServiceTest2() {
        //given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        BuddyDto.PatchRecruitment patchRecruitmentDto = StubData.MockBuddy.getPatchRecruitment();
        BuddyDto.PatchResponseRecruitment responseRecruitmentDto = StubData.MockBuddy.getPatchResponseRecruitment();
        Member member = StubData.MockMember.getMember();
        buddyRecruitment.addMember(member);

        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyRecruitmentRepository.findById(buddyRecruitment.getId())).thenReturn(Optional.of(buddyRecruitment));
        when(buddyMapper.toPatchResponseRecruitmentDto(buddyRecruitment)).thenReturn(responseRecruitmentDto);

        //when
        buddyRecruitmentService.checkWriterAndModifier(buddyRecruitment.getId(), member.getEmail());
        BuddyDto.PatchResponseRecruitment responseRecruitment = buddyRecruitmentService.updateBuddyRecruitment(
                patchRecruitmentDto, buddyRecruitment.getId()
        );

        //then
        assertAll(
                () -> assertEquals(responseRecruitment.getTitle(), buddyRecruitment.getTitle()),
                () -> assertEquals(responseRecruitment.getContent(), buddyRecruitment.getContent()),
                () -> assertEquals(responseRecruitment.getTravelNationality(), buddyRecruitment.getTravelNationality()),
                () -> assertEquals(responseRecruitment.getTravelStartDate(), TimeUtils.localDateTimeToLocalDate(buddyRecruitment.getTravelStartDate())),
                () -> assertEquals(responseRecruitment.getTravelEndDate(), TimeUtils.localDateTimeToLocalDate(buddyRecruitment.getTravelEndDate()))
        );
    }

    @Test
    @DisplayName("동행 모집글 삭제")
    void buddyRecruitmentServiceTest3() {
        //given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        Member member = StubData.MockMember.getMember();
        buddyRecruitment.addMember(member);

        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyRecruitmentRepository.findById(buddyRecruitment.getId())).thenReturn(Optional.of(buddyRecruitment));

        //when
        buddyRecruitmentService.checkWriterAndModifier(buddyRecruitment.getId(), member.getEmail());
        buddyRecruitmentService.deleteBuddyRecruitment(buddyRecruitment.getId());

        //then

    }
}
