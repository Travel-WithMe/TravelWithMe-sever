package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.entity.Buddy;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.recruitment.mapper.RecruitmentMapper;
import com.frog.travelwithme.domain.buddy.repository.BuddyRepository;
import com.frog.travelwithme.domain.recruitment.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.buddy.service.BuddyService;
import com.frog.travelwithme.domain.recruitment.service.RecruitmentService;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
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
class BuddyServiceTest {

    @InjectMocks
    protected BuddyService buddyService;

    @Mock
    protected BuddyRepository buddyRepository;

    @Mock
    protected RecruitmentService recruitmentService;

    @Mock
    protected RecruitmentRepository recruitmentRepository;

    @Mock
    protected RecruitmentMapper buddyMapper;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("동행 매칭신청 (신규)")
    void buddyMatchingServiceTest1() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(member);
        buddy.addRecruitment(recruitment);
        buddy.changeStatus(BuddyStatus.REJECT);

        when(recruitmentService.findRecruitmentById(recruitment.getId())).thenReturn(recruitment);
        doNothing().when(recruitmentService).checkExpiredRecruitment(recruitment);
        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyRepository.findBuddyByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.empty());


        //when
        ResponseBody response = buddyService.requestBuddy(recruitment.getId(), member.getEmail());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.NEW_REQUEST_BUDDY.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.NEW_REQUEST_BUDDY.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭신청 (재신청)")
    void buddyMatchingServiceTest2() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(member);
        buddy.addRecruitment(recruitment);
        buddy.changeStatus(BuddyStatus.REJECT);

        when(recruitmentService.findRecruitmentById(recruitment.getId())).thenReturn(recruitment);
        doNothing().when(recruitmentService).checkExpiredRecruitment(recruitment);
        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyRepository.findBuddyByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.of(buddy));


        //when
        ResponseBody response = buddyService.requestBuddy(recruitment.getId(), member.getEmail());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.RETRY_REQUEST_BUDDY.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.RETRY_REQUEST_BUDDY.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭신청 (신청불가)")
    void buddyMatchingServiceTest3() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(member);
        buddy.addRecruitment(recruitment);
        buddy.changeStatus(BuddyStatus.APPROVE);

        when(recruitmentService.findRecruitmentById(recruitment.getId())).thenReturn(recruitment);
        doNothing().when(recruitmentService).checkExpiredRecruitment(recruitment);
        when(memberService.findMemberAndCheckMemberExists(member.getEmail())).thenReturn(member);
        when(buddyRepository.findBuddyByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.of(buddy));

        //when
        //then
        assertThatThrownBy(() -> buddyService.requestBuddy(recruitment.getId(), member.getEmail()))
                .isInstanceOf(BusinessLogicException.class);

    }

}
