package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.mapper.RecruitmentCommentMapper;
import com.frog.travelwithme.domain.buddy.mapper.RecruitmentMapper;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentCommentRepository;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.buddy.service.RecruitmentCommentService;
import com.frog.travelwithme.domain.buddy.service.RecruitmentService;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
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

import static com.frog.travelwithme.global.enums.EnumCollection.MatchingStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecruitmentCommentServiceTest {

    @InjectMocks
    protected RecruitmentCommentService recruitmentCommentService;

    @Mock
    protected RecruitmentCommentRepository recruitmentCommentRepository;

    @Mock
    protected RecruitmentCommentMapper recruitmentCommentMapper;

    @Mock
    protected RecruitmentService recruitmentService;

    @Mock
    protected MemberService memberService;

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 작성 (회원태그 사용)")
    void recruitmentCommentServiceTest1() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Post postDto = StubData.MockComment.getPostDtoByDepthAndTaggedMemberId(1, 1L);
        RecruitmentCommentDto recruitmentCommentDto = StubData.MockComment.getRecruitmentCommentDto();
        CommentDto.PostResponse postResponseDto = StubData.MockComment.getPostResponseDto();
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitmentComment.addMember(member);
        recruitmentComment.addRecruitment(recruitment);

        Long memberId = member.getId();
        Long recruitmentId = recruitment.getId();
        String memberEmail = member.getEmail();

        when(memberService.findMember(memberId)).thenReturn(member);
        when(memberService.findMember(memberEmail)).thenReturn(member);
        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId)).thenReturn(recruitment);
        when(recruitmentCommentMapper.createRecruitmentCommentDto(postDto)).thenReturn(recruitmentCommentDto);
        when(recruitmentCommentMapper.toEntity(recruitmentCommentDto)).thenReturn(recruitmentComment);
        when(recruitmentCommentRepository.save(recruitmentComment)).thenReturn(recruitmentComment);
        when(recruitmentCommentMapper.toPostResponseCommentDto(recruitmentComment)).thenReturn(postResponseDto);

        //when
        CommentDto.PostResponse response = recruitmentCommentService.createCommentByEmail(
                postDto, recruitmentId, memberEmail
        );

        //then
        assertAll(
                () -> assertEquals(response.getCommentId(), postResponseDto.getCommentId()),
                () -> assertEquals(response.getDepth(), postResponseDto.getDepth()),
                () -> assertEquals(response.getContent(), postResponseDto.getContent()),
                () -> assertEquals(response.getTaggedMemberId(), postResponseDto.getTaggedMemberId())
        );
    }

    @Test
    @DisplayName("동행 모집글 대댓글 작성 (회원태그 미사용)")
    void recruitmentCommentServiceTest2() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Post postDto = StubData.MockComment.getPostDtoByDepthAndTaggedMemberId(1, null);
        RecruitmentCommentDto recruitmentCommentDto = StubData.MockComment.getRecruitmentCommentDto();
        CommentDto.PostResponse postResponseDto = StubData.MockComment.getPostResponseDto();
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitmentComment.addMember(member);
        recruitmentComment.addRecruitment(recruitment);

        Long recruitmentId = recruitment.getId();
        String memberEmail = member.getEmail();

        when(memberService.findMember(memberEmail)).thenReturn(member);
        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId)).thenReturn(recruitment);
        when(recruitmentCommentMapper.createRecruitmentCommentDto(postDto)).thenReturn(recruitmentCommentDto);
        when(recruitmentCommentMapper.toEntity(recruitmentCommentDto)).thenReturn(recruitmentComment);
        when(recruitmentCommentRepository.save(recruitmentComment)).thenReturn(recruitmentComment);
        when(recruitmentCommentMapper.toPostResponseCommentDto(recruitmentComment)).thenReturn(postResponseDto);

        //when
        CommentDto.PostResponse response = recruitmentCommentService.createCommentByEmail(
                postDto, recruitmentId, memberEmail
        );

        //then
        assertAll(
                () -> assertEquals(response.getCommentId(), postResponseDto.getCommentId()),
                () -> assertEquals(response.getDepth(), postResponseDto.getDepth()),
                () -> assertEquals(response.getContent(), postResponseDto.getContent()),
                () -> assertEquals(response.getTaggedMemberId(), postResponseDto.getTaggedMemberId())
        );
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 작성 (태그된 멤버가 존재하지 않는 경우)")
    void recruitmentCommentServiceTest3() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Post postDto = StubData.MockComment.getPostDtoByDepthAndTaggedMemberId(1, 1L);
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitmentComment.addMember(member);
        recruitmentComment.addRecruitment(recruitment);

        Long memberId = member.getId();
        Long recruitmentId = recruitment.getId();
        String memberEmail = member.getEmail();

        doThrow(BusinessLogicException.class).when(memberService).findMember(memberId);

        //when
        //then
        assertThatThrownBy(() -> recruitmentCommentService.createCommentByEmail(
                postDto, recruitmentId, memberEmail
        )).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 작성 (모집이 종료된 or 존재하지 않는 게시글)")
    void recruitmentCommentServiceTest4() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Post postDto = StubData.MockComment.getPostDtoByDepthAndTaggedMemberId(1, 1L);
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitmentComment.addMember(member);
        recruitmentComment.addRecruitment(recruitment);

        Long memberId = member.getId();
        Long recruitmentId = recruitment.getId();
        String memberEmail = member.getEmail();

        when(memberService.findMember(memberId)).thenReturn(member);
        when(memberService.findMember(memberEmail)).thenReturn(member);
        doThrow(BusinessLogicException.class).when(recruitmentService)
                .findRecruitmentByIdAndCheckExpired(recruitmentId);

        //when
        //then
        assertThatThrownBy(() -> recruitmentCommentService.createCommentByEmail(
                postDto, recruitmentId, memberEmail
        )).isInstanceOf(BusinessLogicException.class);
    }
}
