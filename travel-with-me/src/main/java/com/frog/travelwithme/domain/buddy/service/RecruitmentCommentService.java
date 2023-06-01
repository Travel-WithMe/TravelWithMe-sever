package com.frog.travelwithme.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.mapper.RecruitmentCommentMapper;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentCommentRepository;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.common.comment.service.CommentService;
import com.frog.travelwithme.domain.common.comment.dto.CommentTypeDto;
import com.frog.travelwithme.domain.buddy.service.dto.RecruitmentCommentDto;
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
public class RecruitmentCommentService implements CommentService {

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
        RecruitmentCommentDto recruitmentCommentDto = recruitmentCommentMapper.createRecruitmentCommentDto(postDto)
                .addRecruitment(findRecruitment)
                .addMember(findMember);
        return this.createComment(this.createCommentTypeDto(recruitmentCommentDto));
    }

    @Override
    public <T> CommentDto.PostResponse createComment(CommentTypeDto<T> commentTypeDto) {
        RecruitmentCommentDto recruitmentCommentDto = (RecruitmentCommentDto) commentTypeDto.getType();
        RecruitmentComment comment = recruitmentCommentMapper.toEntity(recruitmentCommentDto)
                .addRecruitment(recruitmentCommentDto.getRecruitment())
                .addMember(recruitmentCommentDto.getMember());
        RecruitmentComment savedComment = recruitmentCommentRepository.save(comment);
        return recruitmentCommentMapper.toPostResponseCommentDto(savedComment);
    }

    @Override
    public <T> CommentTypeDto<T> createCommentTypeDto(T commentDto) {
        try {
            CommentTypeDto<RecruitmentCommentDto> commentTypeDto = new CommentTypeDto<>();
            return (CommentTypeDto<T>) commentTypeDto.addType((RecruitmentCommentDto) commentDto);
        } catch (ClassCastException e) {
            log.debug("RecruitmentCommentService.createCommentTypeDto exception occur ClassCastException");
            throw new BusinessLogicException(ExceptionCode.COMMENT_CREATE_IMPOSSIBLE);
        }
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

}
