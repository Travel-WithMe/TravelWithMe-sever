
package com.frog.travelwithme.domain.buddyrecuirtment.service;


import com.frog.travelwithme.domain.buddyrecuirtment.common.DeletionEntity;
import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.mapper.BuddyMapper;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
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
 * 작성일자: 2023/04/11
 **/

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BuddyRecruitmentService {

    private final BuddyRecruitmentRepository buddyRecruitmentRepository;

    private final MemberService memberService;

    private final BuddyMapper buddyMapper;

    public BuddyDto.PostResponseRecruitment createBuddyRecruitment(BuddyDto.PostRecruitment postRecruitmentDto,
                                                                   String email) {

        Member findMember = memberService.findMemberAndCheckMemberExists(email);
        BuddyRecruitment mappedBuddyRecruitment = buddyMapper.toEntity(postRecruitmentDto);
        mappedBuddyRecruitment.addMember(findMember);
        BuddyRecruitment buddyRecruitment = buddyRecruitmentRepository.save(mappedBuddyRecruitment);
        return buddyMapper.toPostResponseRecruitmentDto(buddyRecruitment);

    }

    public BuddyDto.PatchResponseRecruitment updateBuddyRecruitment(BuddyDto.PatchRecruitment patchRecruitmentDto,
                                                                    Long recruitmentsId) {

        BuddyRecruitment buddyRecruitment = this.findBuddyRecruitmentById(recruitmentsId);
        buddyRecruitment.updateBuddyRecruitment(patchRecruitmentDto);
        return buddyMapper.toPatchResponseRecruitmentDto(buddyRecruitment);

    }

    public void deleteBuddyRecruitment(Long recruitmentsId) {
        BuddyRecruitment buddyRecruitment = this.findBuddyRecruitmentById(recruitmentsId);
        buddyRecruitment.updateDeletionEntity();
    }

    public BuddyDto.GetResponseRecruitment findBuddyRecruitment(Long recruitmentsId, String email) {

        BuddyRecruitment findBuddyRecruitment = this.findBuddyRecruitmentById(recruitmentsId);
        Boolean recruitmentRequestStatus = this.checkBuddyRecruitmentWriterByEmail(findBuddyRecruitment, email);
        // TODO : 버디 매칭상태를 확인해서 모집 요청상태 만들기
        // TODO : 버디 매칭상태에서 승인완료된 사람들을 참여인원 찾기
        return null;
    }

    @Transactional(readOnly = true)
    public BuddyRecruitment findBuddyRecruitmentById(Long id) {
        Optional<BuddyRecruitment> findBuddyRecruitment = buddyRecruitmentRepository.findById(id);
        return findBuddyRecruitment.orElseThrow(() -> {
            log.debug("BuddyRecruitmentService.findBuddyRecruitmentById exception occur id: {}", id);
            throw new BusinessLogicException(ExceptionCode.BUDDY_RECRUITMENT_NOT_FOUND);
        });
    }

    @Transactional(readOnly = true)
    public void checkWriterAndModifier(Long recruitmentsId, String email) {
        BuddyRecruitment findBuddyRecruitment = this.findBuddyRecruitmentById(recruitmentsId);
        Member writer = findBuddyRecruitment.getMember();
        Member modifier = memberService.findMemberAndCheckMemberExists(email);
        if(!writer.equals(modifier)) {
            log.debug("BuddyRecruitmentService.checkWriterAndModifier exception occur " +
                    "writerMember: {}, modifierMember: {}", writer, modifier);
            throw new BusinessLogicException(ExceptionCode.BUDDY_RECRUITMENT_WRITER_NOT_MATCH);
        }
    }

    private Boolean checkBuddyRecruitmentWriterByEmail(BuddyRecruitment buddyRecruitment, String email) {
        Member member = buddyRecruitment.getMember();
        if (member.getEmail().equals(email)) {
            return true;
        }
        return false;
    }
}
