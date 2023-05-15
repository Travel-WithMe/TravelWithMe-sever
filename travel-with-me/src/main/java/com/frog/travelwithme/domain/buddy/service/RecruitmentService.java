
package com.frog.travelwithme.domain.buddy.service;


import com.frog.travelwithme.domain.buddy.mapper.RecruitmentMapper;
import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.frog.travelwithme.global.enums.EnumCollection.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    private final MemberService memberService;

    private final RecruitmentMapper recruitmentMapper;

    public RecruitmentDto.PostResponse createRecruitmentByEmail(RecruitmentDto.Post postDto,
                                                                String email) {

        Member findMember = memberService.findMember(email);
        return this.createRecruitment(postDto, findMember);
    }

    public RecruitmentDto.PatchResponse updateRecruitmentByEmail(RecruitmentDto.Patch patchDto,
                                                                 Long recruitmentId,
                                                                 String email) {
        Recruitment findRecruitment = this.findRecruitmentAndCheckEqualWriterAndUser(recruitmentId, email);
        return this.updateRecruitment(patchDto, findRecruitment);
    }

    public void deleteRecruitmentByEmail(Long recruitmentId, String email) {
        Recruitment findRecruitment = this.findRecruitmentAndCheckEqualWriterAndUser(recruitmentId, email);
        this.deleteRecruitment(findRecruitment);
    }

//    TODO : 댓글, 매칭관련 작업이 끝나면 조회 반환 작성하기
//    public RecruitmentDto.GetResponse findRecruitment(Long recruitmentId, String email) {
//
//        Recruitment findRecruitment = this.findRecruitmentById(recruitmentId);
//        Boolean recruitmentRequestStatus = this.checkRecruitmentWriterByEmail(findRecruitment, email);
//        // TODO : 버디 매칭상태를 확인해서 모집 요청상태 만들기
//        // TODO : 버디 매칭상태에서 승인완료된 사람들을 참여인원 찾기
//        return null;
//    }

    @Transactional(readOnly = true)
    public Recruitment findRecruitmentById(Long id) {
        return recruitmentRepository.findById(id).orElseThrow(() -> {
            log.debug("RecruitmentService.findRecruitmentById exception occur id: {}", id);
            throw new BusinessLogicException(ExceptionCode.RECRUITMENT_NOT_FOUND);
        });
    }

    @Transactional(readOnly = true)
    public Recruitment findRecruitmentByIdJoinMember(Long id) {
        return recruitmentRepository.findRecruitmentByIdJoinMember(id).orElseThrow(() -> {
            log.debug("RecruitmentService.findRecruitmentById exception occur id: {}", id);
            throw new BusinessLogicException(ExceptionCode.RECRUITMENT_NOT_FOUND);
        });
    }

    @Transactional(readOnly = true)
    public Recruitment findRecruitmentByIdAndCheckExpired(Long recruitmentId) {
        Recruitment recruitment = this.findRecruitmentById(recruitmentId);
        this.checkExpiredRecruitment(recruitment);
        return recruitment;
    }

    @Transactional(readOnly = true)
    public Recruitment findRecruitmentAndCheckEqualWriterAndUser(Long recruitmentId, String email) {
        Recruitment recruitment = this.findRecruitmentByIdJoinMember(recruitmentId);
        this.checkEqualWriterAndUser(recruitment, email);
        return recruitment;
    }

    public void checkExpiredRecruitment(Recruitment recruitment) {
        RecruitmentStatus status = recruitment.getRecruitmentStatus();
        if(status.equals(RecruitmentStatus.END)) {
            log.debug("RecruitmentService.checkExpiredRecruitment exception occur " +
                    "recruitment: {}", recruitment);
            throw new BusinessLogicException(ExceptionCode.RECRUITMENT_EXPIRED);
        }
    }

    public void checkEqualWriterAndUser(Recruitment recruitment, String email) {
        Member writer = recruitment.getMember();
        if(!writer.getEmail().equals(email)) {
            log.debug("RecruitmentService.checkEqualWriterAndUser exception occur " +
                    "recruitment: {}, email: {}", recruitment, email);
            throw new BusinessLogicException(ExceptionCode.RECRUITMENT_WRITER_NOT_MATCH);
        }
    }

    private RecruitmentDto.PostResponse createRecruitment(RecruitmentDto.Post postDto, Member member) {

        Recruitment mappedRecruitment = recruitmentMapper.toEntity(postDto).addMember(member);
        return recruitmentMapper.toPostResponseRecruitmentDto(recruitmentRepository.save(mappedRecruitment));
    }

    private RecruitmentDto.PatchResponse updateRecruitment(RecruitmentDto.Patch patchDto, Recruitment recruitment) {

        Recruitment updatedRecruitment = recruitment.updateBuddyRecruitment(patchDto);
        return recruitmentMapper.toPatchResponseRecruitmentDto(updatedRecruitment);
    }

    private void deleteRecruitment(Recruitment recruitment) {
        recruitment.updateDeletionEntity();
    }


//    TODO : 댓글, 매칭관련 작업이 끝나면 조회 반환 작성하기
//    private Boolean checkRecruitmentWriterByEmail(Recruitment recruitment, String email) {
//        Member member = recruitment.getMember();
//        if (member.getEmail().equals(email)) {
//            return true;
//        }
//        return false;
//    }
}
