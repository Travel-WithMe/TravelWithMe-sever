package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.mapper.BuddyMapper;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentServiceImpl;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.utils.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    protected BuddyRecruitmentServiceImpl buddyRecruitmentService;

    @Mock
    protected BuddyRecruitmentRepository buddyRecruitmentRepository;

    @Mock
    protected BuddyMapper buddyMapper;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("createdRecruitment() 테스트")
    void buddyRecruitmentServiceTest1() {
        //given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        BuddyDto.PostRecruitment postRecruitmentDto = StubData.MockBuddy.getPostRecruitment();
        BuddyDto.ResponseRecruitment responseRecruitmentDto = StubData.MockBuddy.getResponseRecruitment();
        Member member = StubData.MockMember.getMember();
        buddyRecruitment.addMember(member);
        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyMapper.toEntity(postRecruitmentDto)).thenReturn(buddyRecruitment);
        when(buddyRecruitmentRepository.save(buddyRecruitment)).thenReturn(buddyRecruitment);
        when(buddyMapper.toDto(buddyRecruitment)).thenReturn(responseRecruitmentDto);

        //when
        BuddyDto.ResponseRecruitment responseRecruitment = buddyRecruitmentService.createdRecruitment(
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

}
