package com.frog.travelwithme.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.entity.Buddy;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.BuddyRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.domain.recruitment.service.RecruitmentService;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
public class BuddyService {

    private final BuddyRepository buddyRepository;

    private final RecruitmentService recruitmentService;

    private final MemberService memberService;

    public ResponseBody requestBuddyByUser(Long recruitmentId, String email) {
        Recruitment recruitment = recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId);
        Member member = memberService.findMemberAndCheckMemberExists(email);
        Optional<Buddy> findBuddy = buddyRepository.findBuddyByMemberAndRecruitment(member, recruitment);
        return this.requestBuddy(findBuddy, recruitment, member);
    }

    public ResponseBody cancelBuddyByUser(Long recruitmentId, String email) {
        Recruitment recruitment = recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId);
        Member member = memberService.findMemberAndCheckMemberExists(email);
        Optional<Buddy> findBuddy = buddyRepository.findBuddyByMemberAndRecruitment(member, recruitment);
        return this.cancelBuddy(findBuddy);
    }

    private ResponseBody requestBuddy(Optional<Buddy> findBuddy, Recruitment recruitment, Member member) {
        if(findBuddy.isEmpty()) {
            this.createBuddy(recruitment, member);
            return ResponseBody.NEW_REQUEST_BUDDY;
        } else {
            Buddy buddy = findBuddy.get();
            this.checkPossibleToRequestBuddy(buddy);
            this.updateBuddyByStatus(buddy, BuddyStatus.CANCEL);
            return ResponseBody.RETRY_REQUEST_BUDDY;
        }
    }

    private ResponseBody cancelBuddy(Optional<Buddy> findBuddy) {
        if(findBuddy.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.BUDDY_NOT_FOUND);
        } else {
            Buddy buddy = findBuddy.get();
            this.checkPossibleToCancelBuddy(buddy);
            this.updateBuddyByStatus(buddy, BuddyStatus.CANCEL);
            return ResponseBody.CANCEL_BUDDY;
        }
    }

    private void createBuddy(Recruitment recruitment, Member member) {
        Buddy buddy = new Buddy(BuddyStatus.WAIT);
        buddy.addMember(member);
        buddy.addRecruitment(recruitment);
        recruitment.addBuddy(buddy);
    }

    private void updateBuddyByStatus(Buddy buddy, BuddyStatus status) {
        if(status.equals(BuddyStatus.WAIT)) {
            buddy.changeWait();
        } else if (status.equals(BuddyStatus.APPROVE)) {
            buddy.changeApprove();
        } else if (status.equals(BuddyStatus.REJECT)) {
            buddy.changeReject();
        } else if (status.equals(BuddyStatus.CANCEL)) {
            buddy.changeCancel();
        }
    }

    private void checkPossibleToRequestBuddy(Buddy buddy) {
        if (buddy.getStatus().equals(BuddyStatus.APPROVE)) {
            log.debug("BuddyService.checkPossibleToRequestBuddy exception occur buddy: {}", buddy);
            throw new BusinessLogicException(ExceptionCode.BUDDY_REQUEST_NOT_ALLOWED);
        }
    }

    private void checkPossibleToCancelBuddy(Buddy buddy) {
        BuddyStatus buddyStatus = buddy.getStatus();
        if (buddyStatus.equals(BuddyStatus.REJECT) || buddyStatus.equals(BuddyStatus.CANCEL)) {
            log.debug("BuddyService.checkPossibleToRequestBuddy exception occur buddy: {}", buddy);
            throw new BusinessLogicException(ExceptionCode.BUDDY_CANCEL_NOT_ALLOWED);
        }
    }

}
