package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.repository.MatchingRepository;
import com.frog.travelwithme.domain.buddy.service.MatchingService;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.mapper.RecruitmentMapper;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.buddy.service.RecruitmentService;
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

import static com.frog.travelwithme.global.enums.EnumCollection.ResponseBody;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @InjectMocks
    protected MatchingService matchingService;

    @Mock
    protected MatchingRepository matchingRepository;

    @Mock
    protected RecruitmentService recruitmentService;

    @Mock
    protected RecruitmentRepository recruitmentRepository;

    @Mock
    protected RecruitmentMapper recruitmentMapper;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("동행 매칭신청 (신규)")
    void matchingServiceTest1() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitment.getId())).thenReturn(recruitment);
        when(memberService.findMember(member.getEmail())).thenReturn(member);
        when(matchingRepository.findMatchingByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.empty());


        //when
        ResponseBody response = matchingService.requestMatchingByEmail(recruitment.getId(), member.getEmail());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.NEW_REQUEST_MATCHING.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.NEW_REQUEST_MATCHING.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭신청 (재신청)")
    void matchingServiceTest2() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(member);
        matching.addRecruitment(recruitment);
        matching.reject();

        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitment.getId())).thenReturn(recruitment);
        when(memberService.findMember(member.getEmail())).thenReturn(member);
        when(matchingRepository.findMatchingByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.of(matching));


        //when
        ResponseBody response = matchingService.requestMatchingByEmail(recruitment.getId(), member.getEmail());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.RETRY_REQUEST_MATCHING.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.RETRY_REQUEST_MATCHING.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭신청 (신청불가)")
    void matchingServiceTest3() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(member);
        matching.addRecruitment(recruitment);
        matching.approve();

        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitment.getId())).thenReturn(recruitment);
        when(memberService.findMember(member.getEmail())).thenReturn(member);
        when(matchingRepository.findMatchingByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.of(matching));

        //when
        //then
        assertThatThrownBy(() -> matchingService.requestMatchingByEmail(recruitment.getId(), member.getEmail()))
                .isInstanceOf(BusinessLogicException.class);

    }

    @Test
    @DisplayName("동행 매칭취소")
    void matchingServiceTest4() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(member);
        matching.addRecruitment(recruitment);
        matching.request();

        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitment.getId())).thenReturn(recruitment);
        when(memberService.findMember(member.getEmail())).thenReturn(member);
        when(matchingRepository.findMatchingByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.of(matching));


        //when
        ResponseBody response = matchingService.cancelMatchingByEmail(recruitment.getId(), member.getEmail());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.CANCEL_MATCHING.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.CANCEL_MATCHING.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭취소 (매칭 이력이 없음)")
    void matchingServiceTest5() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitment.getId())).thenReturn(recruitment);
        when(memberService.findMember(member.getEmail())).thenReturn(member);
        when(matchingRepository.findMatchingByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.empty());


        //when
        //then
        assertThatThrownBy(() -> matchingService.cancelMatchingByEmail(recruitment.getId(), member.getEmail()))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 매칭취소 (취소불가)")
    void matchingServiceTest6() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(member);
        matching.addRecruitment(recruitment);
        matching.reject();

        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitment.getId())).thenReturn(recruitment);
        when(memberService.findMember(member.getEmail())).thenReturn(member);
        when(matchingRepository.findMatchingByMemberAndRecruitment(any(),any()))
                .thenReturn(Optional.of(matching));


        //when
        //then
        assertThatThrownBy(() -> matchingService.cancelMatchingByEmail(recruitment.getId(), member.getEmail()))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 매칭승인")
    void matchingServiceTest7() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        Member user = StubData.MockMember.getMember();
        recruitment.addMember(writer);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(recruitment);
        matching.request();


        when(recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail()))
                .thenReturn(recruitment);
        doNothing().when(recruitmentService).checkExpiredRecruitment(recruitment);
        when(matchingRepository.findById(matching.getId())).thenReturn(Optional.of(matching));


        //when
        ResponseBody response = matchingService.approveMatchingByEmail(recruitment.getId(), writer.getEmail(), matching.getId());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.APPROVE_MATCHING.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.APPROVE_MATCHING.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭승인 (모집이 종료된 게시글)")
    void matchingServiceTest8() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        Member user = StubData.MockMember.getMember();
        recruitment.addMember(writer);
        recruitment.end();

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(recruitment);
        matching.request();


        when(recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail()))
                .thenReturn(recruitment);
        doThrow(BusinessLogicException.class).when(recruitmentService).checkExpiredRecruitment(recruitment);

        //when
        //then
        assertThatThrownBy(() -> matchingService.approveMatchingByEmail(recruitment.getId(), writer.getEmail(), matching.getId()))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 매칭승인 (동행글 작성자가 아님)")
    void matchingServiceTest9() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        Member user = StubData.MockMember.getMember();
        recruitment.addMember(writer);
        recruitment.end();

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(recruitment);
        matching.request();


        doThrow(BusinessLogicException.class).when(recruitmentService)
                .findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail());

        //when
        //then
        assertThatThrownBy(() -> matchingService.approveMatchingByEmail(recruitment.getId(), user.getEmail(), matching.getId()))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 매칭승인 (동행 모집글이 다름)")
    void matchingServiceTest10() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment recruitmentOther = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        recruitment.addMember(writer);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(recruitmentOther);
        matching.request();


        when(recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail()))
                .thenReturn(recruitment);
        doNothing().when(recruitmentService).checkExpiredRecruitment(recruitment);
        when(matchingRepository.findById(matching.getId())).thenReturn(Optional.of(matching));

        //when
        //then
        assertThatThrownBy(() ->
                matchingService
                        .approveMatchingByEmail(recruitment.getId(), writer.getEmail(), matching.getId()))
                        .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 매칭거절")
    void matchingServiceTest11() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        Member user = StubData.MockMember.getMember();
        recruitment.addMember(writer);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(recruitment);
        matching.request();


        when(recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail()))
                .thenReturn(recruitment);
        doNothing().when(recruitmentService).checkExpiredRecruitment(recruitment);
        when(matchingRepository.findById(matching.getId())).thenReturn(Optional.of(matching));


        //when
        ResponseBody response = matchingService.rejectMatchingByEmail(recruitment.getId(), writer.getEmail(), matching.getId());

        //then
        assertAll(
                () -> assertEquals(response.getName(), ResponseBody.REJECT_MATCHING.getName()),
                () -> assertEquals(response.getDescription(), ResponseBody.REJECT_MATCHING.getDescription())
        );
    }

    @Test
    @DisplayName("동행 매칭거절 (모집이 종료된 게시글)")
    void matchingServiceTest12() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        Member user = StubData.MockMember.getMember();
        recruitment.addMember(writer);
        recruitment.end();

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(recruitment);
        matching.request();


        when(recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail()))
                .thenReturn(recruitment);
        doThrow(BusinessLogicException.class).when(recruitmentService).checkExpiredRecruitment(recruitment);

        //when
        //then
        assertThatThrownBy(() -> matchingService.rejectMatchingByEmail(recruitment.getId(), writer.getEmail(), matching.getId()))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 매칭거절 (동행글 작성자가 아님)")
    void matchingServiceTest13() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        Member user = StubData.MockMember.getMember();
        recruitment.addMember(writer);
        recruitment.end();

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(recruitment);
        matching.request();


        doThrow(BusinessLogicException.class).when(recruitmentService)
                .findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail());

        //when
        //then
        assertThatThrownBy(() -> matchingService.rejectMatchingByEmail(recruitment.getId(), user.getEmail(), matching.getId()))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 매칭거절 (동행 모집글이 다름)")
    void matchingServiceTest14() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment recruitmentOther = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMember();
        recruitment.addMember(writer);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(recruitmentOther);
        matching.request();


        when(recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitment.getId(), writer.getEmail()))
                .thenReturn(recruitment);
        doNothing().when(recruitmentService).checkExpiredRecruitment(recruitment);
        when(matchingRepository.findById(matching.getId())).thenReturn(Optional.of(matching));

        //when
        //then
        assertThatThrownBy(() ->
                matchingService
                        .rejectMatchingByEmail(recruitment.getId(), writer.getEmail(), matching.getId()))
                .isInstanceOf(BusinessLogicException.class);
    }
}
