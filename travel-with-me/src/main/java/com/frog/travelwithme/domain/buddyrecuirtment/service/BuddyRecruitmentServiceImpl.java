
package com.frog.travelwithme.domain.buddyrecuirtment.service;


import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.mapper.BuddyMapper;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BuddyRecruitmentServiceImpl implements BuddyRecruitmentService{

    private final BuddyRecruitmentRepository buddyRecruitmentRepository;

    private final MemberService memberService;

    private final BuddyMapper buddyMapper;

    @Override
    public BuddyDto.ResponseRecruitment createdRecruitment(BuddyDto.PostRecruitment postRecruitmentDto, String email) {

        Member findMember = memberService.findMemberAndCheckMemberExists(email);
        BuddyRecruitment mappedBuddyRecruitment = buddyMapper.toEntity(postRecruitmentDto);
        mappedBuddyRecruitment.addMember(findMember);
        BuddyRecruitment buddyRecruitment = buddyRecruitmentRepository.save(mappedBuddyRecruitment);
        return buddyMapper.toDto(buddyRecruitment);

    }
}
