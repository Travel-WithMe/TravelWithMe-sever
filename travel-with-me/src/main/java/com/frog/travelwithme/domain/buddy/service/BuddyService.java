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

    public EnumCollection.ResponseBody requestBuddy(Long recruitmentId, String email) {
        Recruitment recruitment = recruitmentService.findRecruitmentById(recruitmentId);
        recruitmentService.checkExpiredRecruitment(recruitment);

        Member member = memberService.findMemberAndCheckMemberExists(email);
        Optional<Buddy> findBuddy = this.findBuddyByMemberAndRecruitment(
                member, recruitment
        );

        if(this.checkEmptyBuddyMatching(findBuddy)) {
            Buddy buddy = new Buddy(BuddyStatus.WAIT);
            buddy.addMember(member);
            buddy.addRecruitment(recruitment);
            recruitment.addBuddy(buddy);
            return EnumCollection.ResponseBody.NEW_REQUEST_BUDDY;
        } else {
            findBuddy.get().changeWait();
            return EnumCollection.ResponseBody.RETRY_REQUEST_BUDDY;
        }

    }

    public Optional<Buddy> findBuddyByMemberAndRecruitment(Member member,
                                                          Recruitment recruitment) {
        return buddyRepository.findBuddyByMemberAndRecruitment(member, recruitment);
    }

    private boolean checkEmptyBuddyMatching(Optional<Buddy> buddy) {
        boolean check;
        if (buddy.isEmpty()) {
            check = true;
        } else if (buddy.get().getStatus().equals(BuddyStatus.REJECT)) {
            check = false;
        } else {
            log.debug("BuddyMatchingService.requestBuddy exception occur buddy: {}", buddy);
            throw new BusinessLogicException(ExceptionCode.BUDDY_REQUEST_NOT_ALLOWED);
        }
        return check;
    }

}
