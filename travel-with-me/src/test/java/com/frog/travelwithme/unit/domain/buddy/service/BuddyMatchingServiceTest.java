package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.mapper.BuddyMapper;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyMatchingRepository;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyMatchingService;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentService;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.utils.StubData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@ExtendWith(MockitoExtension.class)
class BuddyMatchingServiceTest {

    @InjectMocks
    protected BuddyMatchingService buddyMatchingService;

    @Mock
    protected BuddyMatchingRepository buddyMatchingRepository;

    @Mock
    protected BuddyRecruitmentService buddyRecruitmentService;

    @Mock
    protected BuddyRecruitmentRepository buddyRecruitmentRepository;

    @Mock
    protected BuddyMapper buddyMapper;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("동행 매칭신청 (신규)")
    void buddyMatchingServiceTest1() {
        //given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        Member member = StubData.MockMember.getMember();
        buddyRecruitment.addMember(member);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(member);
        buddyMatching.addBuddyRecruitment(buddyRecruitment);
        buddyMatching.changeStatus(BuddyMatchingStatus.REJECT);

        when(buddyRecruitmentService.findBuddyRecruitmentById(buddyRecruitment.getId())).thenReturn(buddyRecruitment);
        doNothing().when(buddyRecruitmentService).checkExpiredBuddyRecruitment(buddyRecruitment);
        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyMatchingRepository.findBuddyMatchingByMemberAndBuddyRecruitment(any(),any()))
                .thenReturn(Optional.empty());


        //when
        ResponseBody response = buddyMatchingService.requestMatching(buddyRecruitment.getId(), member.getEmail());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.NEW_REQUEST_MATCHING.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.NEW_REQUEST_MATCHING.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭신청 (재신청)")
    void buddyMatchingServiceTest2() {
        //given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        Member member = StubData.MockMember.getMember();
        buddyRecruitment.addMember(member);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(member);
        buddyMatching.addBuddyRecruitment(buddyRecruitment);
        buddyMatching.changeStatus(BuddyMatchingStatus.REJECT);

        when(buddyRecruitmentService.findBuddyRecruitmentById(buddyRecruitment.getId())).thenReturn(buddyRecruitment);
        doNothing().when(buddyRecruitmentService).checkExpiredBuddyRecruitment(buddyRecruitment);
        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyMatchingRepository.findBuddyMatchingByMemberAndBuddyRecruitment(any(),any()))
                .thenReturn(Optional.of(buddyMatching));


        //when
        ResponseBody response = buddyMatchingService.requestMatching(buddyRecruitment.getId(), member.getEmail());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.RETRY_REQUEST_MATCHING.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.RETRY_REQUEST_MATCHING.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭신청 (신청불가)")
    void buddyMatchingServiceTest3() {
        //given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        Member member = StubData.MockMember.getMember();
        buddyRecruitment.addMember(member);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(member);
        buddyMatching.addBuddyRecruitment(buddyRecruitment);
        buddyMatching.changeStatus(BuddyMatchingStatus.APPROVE);

        when(buddyRecruitmentService.findBuddyRecruitmentById(buddyRecruitment.getId())).thenReturn(buddyRecruitment);
        doNothing().when(buddyRecruitmentService).checkExpiredBuddyRecruitment(buddyRecruitment);
        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyMatchingRepository.findBuddyMatchingByMemberAndBuddyRecruitment(any(),any()))
                .thenReturn(Optional.of(buddyMatching));

        //when
        //then
        assertThatThrownBy(() -> buddyMatchingService.requestMatching(buddyRecruitment.getId(), member.getEmail()))
                .isInstanceOf(BusinessLogicException.class);

    }

}
