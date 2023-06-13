package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.mapper.RecruitmentCommentMapper;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentCommentRepository;
import com.frog.travelwithme.domain.buddy.service.RecruitmentCommentService;
import com.frog.travelwithme.domain.buddy.service.RecruitmentService;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentCreateDto;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentUpdateDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        RecruitmentCommentCreateDto recruitmentCommentCreateDto = StubData.MockComment.getRecruitmentCommentCreateDto();
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
        when(recruitmentCommentMapper.postDtoToRecruitmentCommentCreateDto(postDto)).thenReturn(recruitmentCommentCreateDto);
        when(recruitmentCommentMapper.toEntity(recruitmentCommentCreateDto)).thenReturn(recruitmentComment);
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
        RecruitmentCommentCreateDto recruitmentCommentCreateDto = StubData.MockComment.getRecruitmentCommentCreateDto();
        CommentDto.PostResponse postResponseDto = StubData.MockComment.getPostResponseDto();
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitmentComment.addMember(member);
        recruitmentComment.addRecruitment(recruitment);

        Long recruitmentId = recruitment.getId();
        String memberEmail = member.getEmail();

        when(memberService.findMember(memberEmail)).thenReturn(member);
        when(recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId)).thenReturn(recruitment);
        when(recruitmentCommentMapper.postDtoToRecruitmentCommentCreateDto(postDto)).thenReturn(recruitmentCommentCreateDto);
        when(recruitmentCommentMapper.toEntity(recruitmentCommentCreateDto)).thenReturn(recruitmentComment);
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
    @DisplayName("동행 모집글 댓글,대댓글 수정 (회원태그 사용)")
    void recruitmentCommentServiceTest4() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경확인", 1L);
        RecruitmentCommentUpdateDto recruitmentCommentUpdateDto = StubData.MockComment.getRecruitmentCommentUpdateDto();
        CommentDto.PatchResponse patchResponseDto = StubData.MockComment.getPatchResponseDto();
        Member member = StubData.MockMember.getMember();
        recruitmentComment.addMember(member);

        Long commentId = recruitmentComment.getId();
        String memberEmail = member.getEmail();

        when(recruitmentCommentRepository.findById(commentId)).thenReturn(Optional.of(recruitmentComment));
        when(recruitmentCommentMapper.patchDtoToRecruitmentCommentUpdateDto(patchDto))
                .thenReturn(recruitmentCommentUpdateDto);
        when(recruitmentCommentMapper.toPatchResponseCommentDto(recruitmentComment)).thenReturn(patchResponseDto);


        //when
        CommentDto.PatchResponse response = recruitmentCommentService.updateCommentByEmail(
                patchDto, commentId, memberEmail
        );

        //then
        assertAll(
                () -> assertEquals(response.getCommentId(), patchResponseDto.getCommentId()),
                () -> assertEquals(response.getDepth(), patchResponseDto.getDepth()),
                () -> assertEquals(response.getContent(), patchResponseDto.getContent()),
                () -> assertEquals(response.getTaggedMemberId(), patchResponseDto.getTaggedMemberId())
        );

        verify(recruitmentCommentRepository, times(1)).findById(commentId);
        verify(recruitmentCommentMapper, times(1))
                .patchDtoToRecruitmentCommentUpdateDto(patchDto);
        verify(recruitmentCommentMapper, times(1))
                .toPatchResponseCommentDto(recruitmentComment);
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 수정 (회원태그 미사용)")
    void recruitmentCommentServiceTest5() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경확인", null);
        RecruitmentCommentUpdateDto recruitmentCommentUpdateDto = StubData.MockComment.getRecruitmentCommentUpdateDto();
        CommentDto.PatchResponse patchResponseDto = StubData.MockComment.getPatchResponseDto();
        Member member = StubData.MockMember.getMember();
        recruitmentComment.addMember(member);

        Long commentId = recruitmentComment.getId();
        String memberEmail = member.getEmail();

        when(recruitmentCommentRepository.findById(commentId)).thenReturn(Optional.of(recruitmentComment));
        when(recruitmentCommentMapper.patchDtoToRecruitmentCommentUpdateDto(patchDto))
                .thenReturn(recruitmentCommentUpdateDto);
        when(recruitmentCommentMapper.toPatchResponseCommentDto(recruitmentComment)).thenReturn(patchResponseDto);


        //when
        CommentDto.PatchResponse response = recruitmentCommentService.updateCommentByEmail(
                patchDto, commentId, memberEmail
        );

        //then
        assertAll(
                () -> assertEquals(response.getCommentId(), patchResponseDto.getCommentId()),
                () -> assertEquals(response.getDepth(), patchResponseDto.getDepth()),
                () -> assertEquals(response.getContent(), patchResponseDto.getContent()),
                () -> assertEquals(response.getTaggedMemberId(), patchResponseDto.getTaggedMemberId())
        );

        verify(recruitmentCommentRepository, times(1)).findById(commentId);
        verify(recruitmentCommentMapper, times(1))
                .patchDtoToRecruitmentCommentUpdateDto(patchDto);
        verify(recruitmentCommentMapper, times(1))
                .toPatchResponseCommentDto(recruitmentComment);
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 수정 (댓글이 존재하지 않는경우)")
    void recruitmentCommentServiceTest6() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경확인", null);
        Member member = StubData.MockMember.getMember();
        recruitmentComment.addMember(member);

        Long commentId = recruitmentComment.getId();
        String memberEmail = member.getEmail();

        when(recruitmentCommentRepository.findById(commentId)).thenReturn(Optional.empty());


        //when
        //then
        assertThatThrownBy(() -> recruitmentCommentService.updateCommentByEmail(
                patchDto, commentId, memberEmail
        )).isInstanceOf(BusinessLogicException.class);

        verify(recruitmentCommentRepository, times(1)).findById(commentId);
        verify(recruitmentCommentMapper, never())
                .patchDtoToRecruitmentCommentUpdateDto(patchDto);
        verify(recruitmentCommentMapper, never())
                .toPatchResponseCommentDto(recruitmentComment);
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 수정 (댓글이 작성자가 일치하지 않는 경우)")
    void recruitmentCommentServiceTest7() {
        //given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경확인", null);
        Member writer = StubData.MockMember.getMemberByEmailAndNickname("dhfif718@naver.com", "이재혁");
        Member member = StubData.MockMember.getMember();
        recruitmentComment.addMember(writer);

        Long commentId = recruitmentComment.getId();
        String memberEmail = member.getEmail();

        when(recruitmentCommentRepository.findById(commentId)).thenReturn(Optional.of(recruitmentComment));

        //when
        //then
        assertThatThrownBy(() -> recruitmentCommentService.updateCommentByEmail(
                patchDto, commentId, memberEmail
        )).isInstanceOf(BusinessLogicException.class);

        verify(recruitmentCommentRepository, times(1)).findById(commentId);
        verify(recruitmentCommentMapper, never())
                .patchDtoToRecruitmentCommentUpdateDto(patchDto);
        verify(recruitmentCommentMapper, never())
                .toPatchResponseCommentDto(recruitmentComment);
    }
}
