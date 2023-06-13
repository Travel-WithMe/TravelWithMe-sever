package com.frog.travelwithme.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.mapper.RecruitmentCommentMapper;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentCommentRepository;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentUpdateDto;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.comment.service.CommentService;
import com.frog.travelwithme.domain.common.comment.dto.CommentTypeDto;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentCreateDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/24
 **/

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentCommentService extends CommentService {

    private final RecruitmentCommentRepository recruitmentCommentRepository;

    private final RecruitmentCommentMapper recruitmentCommentMapper;

    private final MemberService memberService;

    private final RecruitmentService recruitmentService;

    public CommentDto.PostResponse createCommentByEmail(CommentDto.Post postDto,
                                                        Long recruitmentId,
                                                        String email) {
        this.checkExistTaggedMemberId(postDto);
        Member findMember = memberService.findMember(email);
        Recruitment findRecruitment = recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId);
        RecruitmentCommentCreateDto recruitmentCommentCreateDto =
                recruitmentCommentMapper.postDtoToRecruitmentCommentCreateDto(postDto)
                        .addRecruitment(findRecruitment)
                        .addMember(findMember);
        return this.createComment(super.createCommentTypeDto(recruitmentCommentCreateDto));
    }

    public CommentDto.PatchResponse updateCommentByEmail(CommentDto.Patch patchDto,
                                                         Long commentId,
                                                         String email) {
        RecruitmentComment findRecruitmentComment = this.findRecruitmentCommentById(commentId);
        this.checkEqualWriterAndUser(findRecruitmentComment, email);
        RecruitmentCommentUpdateDto recruitmentCommentUpdateDto =
                recruitmentCommentMapper.patchDtoToRecruitmentCommentUpdateDto(patchDto)
                        .addRecruitmentComment(findRecruitmentComment);
        return this.updateComment(super.createCommentTypeDto(recruitmentCommentUpdateDto));
    }

    @Override
    public <T> CommentDto.PostResponse createComment(CommentTypeDto<T> commentTypeDto) {
        RecruitmentCommentCreateDto recruitmentCommentCreateDto = (RecruitmentCommentCreateDto) commentTypeDto.getType();
        RecruitmentComment comment = recruitmentCommentMapper.toEntity(recruitmentCommentCreateDto)
                .addRecruitment(recruitmentCommentCreateDto.getRecruitment())
                .addMember(recruitmentCommentCreateDto.getMember());
        RecruitmentComment savedComment = recruitmentCommentRepository.save(comment);
        return recruitmentCommentMapper.toPostResponseCommentDto(savedComment);
    }

    @Override
    public <T> CommentDto.PatchResponse updateComment(CommentTypeDto<T> commentTypeDto) {
        RecruitmentCommentUpdateDto recruitmentCommentUpdateDto = (RecruitmentCommentUpdateDto) commentTypeDto.getType();
        RecruitmentComment recruitmentComment = recruitmentCommentUpdateDto.getRecruitmentComment()
                .updateRecruitmentComment(recruitmentCommentUpdateDto);
        return recruitmentCommentMapper.toPatchResponseCommentDto(recruitmentComment);
    }

    @Transactional(readOnly = true)
    public RecruitmentComment findRecruitmentCommentById(Long id) {
        return recruitmentCommentRepository.findById(id).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND)
        );
    }

    public void checkExistTaggedMemberId(CommentDto.Post postDto) {
        if (Optional.ofNullable(postDto.getTaggedMemberId()).isPresent()) {
            try {
                memberService.findMember(postDto.getTaggedMemberId());
            } catch (BusinessLogicException e) {
                log.debug("RecruitmentCommentService.checkExistTaggedMemberId exception occur postDto: {}", postDto);
                throw new BusinessLogicException(ExceptionCode.TAGGED_MEMBER_NOT_FOUND);
            }
        }
    }

    public void checkEqualWriterAndUser(RecruitmentComment recruitmentComment, String email) {
        Member writer = recruitmentComment.getMember();
        if (!writer.getEmail().equals(email)) {
            log.debug("RecruitmentCommentService.checkEqualWriterAndUser exception occur " +
                    "recruitmentComment: {}, email: {}", recruitmentComment, email);
            throw new BusinessLogicException(ExceptionCode.COMMENT_WRITER_NOT_MATCH);
        }
    }
}
