package com.frog.travelwithme.domain.buddy.service;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.repository.MatchingRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.MatchingStatus;
import static com.frog.travelwithme.global.enums.EnumCollection.ResponseBody;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;

    private final RecruitmentService recruitmentService;

    private final MemberService memberService;

    public ResponseBody requestMatchingByEmail(Long recruitmentId, String email) {
        Recruitment recruitment = recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId);
        Member member = memberService.findMember(email);
        Optional<Matching> findMatching = matchingRepository.findMatchingByMemberAndRecruitment(member, recruitment);
        return this.requestMatching(findMatching, recruitment, member);
    }

    public ResponseBody cancelMatchingByEmail(Long recruitmentId, String email) {
        Recruitment recruitment = recruitmentService.findRecruitmentByIdAndCheckExpired(recruitmentId);
        Member member = memberService.findMember(email);
        Optional<Matching> findMatching = matchingRepository.findMatchingByMemberAndRecruitment(member, recruitment);
        return this.cancelMatching(findMatching);
    }

    public ResponseBody approveMatchingByEmail(Long recruitmentId, String email, Long matchingId) {
        Recruitment recruitment = recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitmentId, email);
        recruitmentService.checkExpiredRecruitment(recruitment);
        Matching findMatching = this.findMatchingByIdAndCheckEqualRecruitment(matchingId, recruitment);
        return this.approveMatching(findMatching);
    }

    public ResponseBody rejectMatchingByEmail(Long recruitmentId, String email, Long matchingId) {
        Recruitment recruitment = recruitmentService.findRecruitmentAndCheckEqualWriterAndUser(recruitmentId, email);
        recruitmentService.checkExpiredRecruitment(recruitment);
        Matching findMatching = this.findMatchingByIdAndCheckEqualRecruitment(matchingId, recruitment);
        return this.rejectMatching(findMatching);
    }

    private ResponseBody requestMatching(Optional<Matching> findMatching, Recruitment recruitment, Member member) {
        if(findMatching.isEmpty()) {
            this.createMatching(recruitment, member);
            return ResponseBody.NEW_REQUEST_MATCHING;
        } else {
            Matching matching = findMatching.get();
            this.checkPossibleToRequestMatching(matching);
            this.updateMatchingByStatus(matching, MatchingStatus.CANCEL);
            return ResponseBody.RETRY_REQUEST_MATCHING;
        }
    }

    private ResponseBody cancelMatching(Optional<Matching> findMatching) {
        if(findMatching.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MATCHING_NOT_FOUND);
        } else {
            Matching matching = findMatching.get();
            this.checkPossibleToCancelMatching(matching);
            this.updateMatchingByStatus(matching, MatchingStatus.CANCEL);
            return ResponseBody.CANCEL_MATCHING;
        }
    }

    private ResponseBody approveMatching(Matching matching) {
        this.checkPossibleToApproveMatching(matching);
        matching.approve();
        return ResponseBody.APPROVE_MATCHING;
    }

    private ResponseBody rejectMatching(Matching matching) {
        this.checkPossibleToRejectMatching(matching);
        matching.reject();
        return ResponseBody.REJECT_MATCHING;
    }

    private void createMatching(Recruitment recruitment, Member member) {
        Matching matching = new Matching(MatchingStatus.REQUEST);
        matching.addMember(member);
        matching.addRecruitment(recruitment);
        recruitment.addMatching(matching);
    }

    private void updateMatchingByStatus(Matching matching, MatchingStatus status) {
        if(status.equals(MatchingStatus.REQUEST)) {
            matching.request();
        } else if (status.equals(MatchingStatus.APPROVE)) {
            matching.approve();
        } else if (status.equals(MatchingStatus.REJECT)) {
            matching.reject();
        } else if (status.equals(MatchingStatus.CANCEL)) {
            matching.cancel();
        }
    }

    private void checkPossibleToRequestMatching(Matching matching) {
        if (matching.getStatus().equals(MatchingStatus.APPROVE)) {
            log.debug("MatchingService.checkPossibleToRequestMatching exception occur matching: {}", matching);
            throw new BusinessLogicException(ExceptionCode.MATCHING_REQUEST_NOT_ALLOWED);
        }
    }

    private void checkPossibleToCancelMatching(Matching matching) {
        MatchingStatus matchingStatus = matching.getStatus();
        if (matchingStatus.equals(MatchingStatus.REJECT) || matchingStatus.equals(MatchingStatus.CANCEL)) {
            log.debug("MatchingService.checkPossibleToCancelMatching exception occur matching: {}", matching);
            throw new BusinessLogicException(ExceptionCode.MATCHING_CANCEL_NOT_ALLOWED);
        }
    }

    private void checkPossibleToApproveMatching(Matching matching) {
        MatchingStatus matchingStatus = matching.getStatus();
        if (!matchingStatus.equals(MatchingStatus.REQUEST)) {
            log.debug("MatchingService.checkPossibleToApproveMatching exception occur matching: {}", matching);
            throw new BusinessLogicException(ExceptionCode.MATCHING_APPROVE_NOT_ALLOWED);
        }
    }

    private void checkPossibleToRejectMatching(Matching matching) {
        MatchingStatus matchingStatus = matching.getStatus();
        if (!matchingStatus.equals(MatchingStatus.REQUEST)) {
            log.debug("MatchingService.checkPossibleToRejectMatching exception occur matching: {}", matching);
            throw new BusinessLogicException(ExceptionCode.MATCHING_REJECT_NOT_ALLOWED);
        }
    }

    private Matching findMatchingByIdAndCheckEqualRecruitment(Long id, Recruitment recruitment) {
        Matching matching = this.findMatchingById(id);
        if(!matching.getRecruitment().equals(recruitment)) {
            log.debug("MatchingService.findMatchingByIdAndCheckEqualRecruitment exception occur id: {}, recruitment: {}",
                    id, recruitment);
            throw new BusinessLogicException(ExceptionCode.MATCHING_RECRUITMENT_IS_DIFFERENT);
        }
        return matching;
    }

    private Matching findMatchingById(Long id) {
        return matchingRepository.findById(id).orElseThrow(() -> {
            log.debug("MatchingService.findMatchingById exception occur id: {}", id);
            throw new BusinessLogicException(ExceptionCode.MATCHING_NOT_FOUND);
        });
    }

}
