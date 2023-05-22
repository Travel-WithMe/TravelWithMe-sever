package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.mapper.RecruitmentMapper;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.buddy.service.RecruitmentService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.*;
import static org.assertj.core.api.Assertions.assertThat;
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
    protected RecruitmentMapper recruitmentMapper;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("동행 모집글 작성")
    void recruitmentServiceTest1() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.Post postDto = StubData.MockRecruitment.getPostRecruitment();
        RecruitmentDto.PostResponse responseRecruitmentDto = StubData.MockRecruitment.getPostResponseRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(memberService.findMember(member.getEmail())).thenReturn(member);
        when(recruitmentMapper.toEntity(postDto)).thenReturn(recruitment);
        when(recruitmentRepository.save(recruitment)).thenReturn(recruitment);
        when(recruitmentMapper.toPostResponseRecruitmentDto(recruitment)).thenReturn(responseRecruitmentDto);

        //when
        RecruitmentDto.PostResponse responseRecruitment = recruitmentService.createRecruitmentByEmail(
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
    void recruitmentServiceTest2() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.Patch patchDto = StubData.MockRecruitment.getPatchRecruitment();
        RecruitmentDto.PatchResponse responseRecruitmentDto = StubData.MockRecruitment.getPatchResponseRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));
        when(recruitmentMapper.toPatchResponseRecruitmentDto(recruitment)).thenReturn(responseRecruitmentDto);

        //when
        RecruitmentDto.PatchResponse responseRecruitment = recruitmentService.updateRecruitmentByEmail(
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
    void recruitmentServiceTest3() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.Patch patchDto = StubData.MockRecruitment.getPatchRecruitment();
        StubData.MockRecruitment.getPatchResponseRecruitment();
        Member writer = StubData.MockMember.getMemberByEmailAndNickname("dhfif718@naver.com", "LJH");
        Member user = StubData.MockMember.getMemberByEmailAndNickname("kkd718@gmail.com", "KCB");
        recruitment.addMember(writer);

        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.updateRecruitmentByEmail(
                        patchDto,
                        recruitment.getId(),
                        user.getEmail()
                )
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 모집글 삭제")
    void recruitmentServiceTest4() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member member = StubData.MockMember.getMember();
        recruitment.addMember(member);

        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));

        //when
        recruitmentService.deleteRecruitmentByEmail(recruitment.getId(), member.getEmail());

        //then

    }

    @Test
    @DisplayName("동행 모집글 삭제 - 작성자, 유저 불일치")
    void recruitmentServiceTest5() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = StubData.MockMember.getMemberByEmailAndNickname("dhfif718@naver.com", "LJH");
        Member user = StubData.MockMember.getMemberByEmailAndNickname("kkd718@gmail.com", "KCB");
        recruitment.addMember(writer);

        when(recruitmentRepository.findById(recruitment.getId())).thenReturn(Optional.of(recruitment));

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.deleteRecruitmentByEmail(recruitment.getId(), user.getEmail())
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 모집글 매칭신청 회원 리스트 조회")
    void recruitmentServiceTest6() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.MatchingMemberResponse matchingMemberResponse1
                = StubData.MockMember.getMatchingRequestMemberResponse(1L, "dhfif718");
        RecruitmentDto.MatchingMemberResponse matchingMemberResponse2
                = StubData.MockMember.getMatchingRequestMemberResponse(2L, "kkd718");
        RecruitmentDto.MatchingMemberResponse matchingMemberResponse3
                = StubData.MockMember.getMatchingRequestMemberResponse(3L, "리젤란");

        List<RecruitmentDto.MatchingMemberResponse> matchingMemberResponseList = new ArrayList<>();
        matchingMemberResponseList.add(matchingMemberResponse1);
        matchingMemberResponseList.add(matchingMemberResponse2);
        matchingMemberResponseList.add(matchingMemberResponse3);
        Member writer = StubData.MockMember.getMember();
        Member user1 = StubData.MockMember.getMember();
        Member user2 = StubData.MockMember.getMember();
        Member user3 = StubData.MockMember.getMember();
        recruitment.addMember(writer);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user1);
        matching.addMember(user2);
        matching.addMember(user3);
        matching.addRecruitment(recruitment);
        matching.request();
        recruitment.addMatching(matching);
        Long recruitmentId = recruitment.getId();


        when(recruitmentRepository.findRecruitmentByIdAndMatchingStatus(recruitmentId, MatchingStatus.REQUEST))
                .thenReturn(Optional.of(recruitment));
        when(recruitmentMapper.toMatchingMemberResponseRecruitmentDtoList(recruitment.getMatchingList()))
                .thenReturn(matchingMemberResponseList);

        //when
         List<RecruitmentDto.MatchingMemberResponse> response
                 = recruitmentService.getMatchingRequestMemberList(recruitmentId);

        //then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(0).getNickname()).isEqualTo(matchingMemberResponse1.getNickname());
        assertThat(response.get(1).getNickname()).isEqualTo(matchingMemberResponse2.getNickname());
        assertThat(response.get(2).getNickname()).isEqualTo(matchingMemberResponse3.getNickname());
    }

    @Test
    @DisplayName("동행 모집글 매칭신청 회원 리스트 조회 (모집이 종료된 게시글)")
    void recruitmentServiceTest7() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.end();
        Long recruitmentId = recruitment.getId();

        when(recruitmentRepository.findRecruitmentByIdAndMatchingStatus(recruitmentId, MatchingStatus.REQUEST))
                .thenReturn(Optional.of(recruitment));

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.getMatchingRequestMemberList(recruitmentId)
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 모집글 매칭신청 회원 리스트 조회 (동행 모집글이 없음)")
    void recruitmentServiceTest8() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Long recruitmentId = recruitment.getId();

        when(recruitmentRepository.findRecruitmentByIdAndMatchingStatus(recruitmentId, MatchingStatus.REQUEST))
                .thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.getMatchingRequestMemberList(recruitmentId)
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 모집글 매칭승인 회원 리스트 조회")
    void recruitmentServiceTest9() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        RecruitmentDto.MatchingMemberResponse matchingMemberResponse1
                = StubData.MockMember.getMatchingRequestMemberResponse(1L, "dhfif718");
        RecruitmentDto.MatchingMemberResponse matchingMemberResponse2
                = StubData.MockMember.getMatchingRequestMemberResponse(2L, "kkd718");
        RecruitmentDto.MatchingMemberResponse matchingMemberResponse3
                = StubData.MockMember.getMatchingRequestMemberResponse(3L, "리젤란");

        List<RecruitmentDto.MatchingMemberResponse> matchingMemberResponseList = new ArrayList<>();
        matchingMemberResponseList.add(matchingMemberResponse1);
        matchingMemberResponseList.add(matchingMemberResponse2);
        matchingMemberResponseList.add(matchingMemberResponse3);
        Member writer = StubData.MockMember.getMember();
        Member user1 = StubData.MockMember.getMember();
        Member user2 = StubData.MockMember.getMember();
        Member user3 = StubData.MockMember.getMember();
        recruitment.addMember(writer);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user1);
        matching.addMember(user2);
        matching.addMember(user3);
        matching.addRecruitment(recruitment);
        matching.approve();
        recruitment.addMatching(matching);
        Long recruitmentId = recruitment.getId();


        when(recruitmentRepository.findRecruitmentByIdAndMatchingStatus(recruitmentId, MatchingStatus.APPROVE))
                .thenReturn(Optional.of(recruitment));
        when(recruitmentMapper.toMatchingMemberResponseRecruitmentDtoList(recruitment.getMatchingList()))
                .thenReturn(matchingMemberResponseList);

        //when
        List<RecruitmentDto.MatchingMemberResponse> response
                = recruitmentService.getMatchingApprovedMemberList(recruitmentId);

        //then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.get(0).getNickname()).isEqualTo(matchingMemberResponse1.getNickname());
        assertThat(response.get(1).getNickname()).isEqualTo(matchingMemberResponse2.getNickname());
        assertThat(response.get(2).getNickname()).isEqualTo(matchingMemberResponse3.getNickname());
    }

    @Test
    @DisplayName("동행 모집글 매칭승인 회원 리스트 조회 (모집이 종료된 게시글)")
    void recruitmentServiceTest10() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.end();
        Long recruitmentId = recruitment.getId();

        when(recruitmentRepository.findRecruitmentByIdAndMatchingStatus(recruitmentId, MatchingStatus.APPROVE))
                .thenReturn(Optional.of(recruitment));

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.getMatchingApprovedMemberList(recruitmentId)
        ).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 모집글 매칭승인 회원 리스트 조회 (동행 모집글이 없음)")
    void recruitmentServiceTest11() {
        //given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Long recruitmentId = recruitment.getId();

        when(recruitmentRepository.findRecruitmentByIdAndMatchingStatus(recruitmentId, MatchingStatus.APPROVE))
                .thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
                () -> recruitmentService.getMatchingApprovedMemberList(recruitmentId)
        ).isInstanceOf(BusinessLogicException.class);
    }
}
