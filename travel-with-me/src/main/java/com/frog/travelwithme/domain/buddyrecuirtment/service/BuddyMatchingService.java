package com.frog.travelwithme.domain.buddyrecuirtment.service;

import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyMatchingRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
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
public class BuddyMatchingService {

    private final BuddyMatchingRepository buddyMatchingRepository;

    private final BuddyRecruitmentService buddyRecruitmentService;

    private final MemberService memberService;

    public EnumCollection.ResponseBody requestMatching(Long recruitmentId, String email) {
        BuddyRecruitment buddyRecruitment = buddyRecruitmentService.findBuddyRecruitmentById(recruitmentId);
        buddyRecruitmentService.checkExpiredBuddyRecruitment(buddyRecruitment);

        Member member = memberService.findMemberAndCheckMemberExists(email);
        Optional<BuddyMatching> findBuddyMatching = this.findBuddyMatchingByMemberAndBuddyRecruitment(
                member, buddyRecruitment
        );

        if(this.checkEmptyBuddyMatching(findBuddyMatching)) {
            BuddyMatching buddyMatching = new BuddyMatching(BuddyMatchingStatus.WAIT);
            buddyMatching.addMember(member);
            buddyMatching.addBuddyRecruitment(buddyRecruitment);
            buddyRecruitment.addBuddyMatching(buddyMatching);
            return EnumCollection.ResponseBody.NEW_REQUEST_MATCHING;
        } else {
            findBuddyMatching.get().changeStatus(BuddyMatchingStatus.WAIT);
            return EnumCollection.ResponseBody.RETRY_REQUEST_MATCHING;
        }

    }

    public Optional<BuddyMatching> findBuddyMatchingByMemberAndBuddyRecruitment(Member member,
                                                                               BuddyRecruitment buddyRecruitment) {
        return buddyMatchingRepository.findBuddyMatchingByMemberAndBuddyRecruitment(member, buddyRecruitment);
    }

    private boolean checkEmptyBuddyMatching(Optional<BuddyMatching> buddyMatching) {
        boolean check;
        if (buddyMatching.isEmpty()) {
            check = true;
        } else if (buddyMatching.get().getStatus().equals(BuddyMatchingStatus.REJECT)) {
            check = false;
        } else {
            log.debug("BuddyMatchingService.requestMatching exception occur buddyMatching: {}", buddyMatching);
            throw new BusinessLogicException(ExceptionCode.BUDDY_MATCHING_REQUEST_NOT_ALLOWED);
        }
        return check;
    }

}
