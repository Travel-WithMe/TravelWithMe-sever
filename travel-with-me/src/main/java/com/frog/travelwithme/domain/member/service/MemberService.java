package com.frog.travelwithme.domain.member.service;

import com.frog.travelwithme.common.utils.CustomBeanUtils;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.mapper.MemberMapper;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.frog.travelwithme.domain.member.entity.Member.OAuthStatus.NORMAL;
import static com.frog.travelwithme.global.security.auth.utils.CustomAuthorityUtils.verifiedRole;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.1
 * 작성일자: 2023/03/29
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomBeanUtils<Member> beanUtils;
    private final MemberMapper mapper;

    public MemberDto.Response signUp(MemberDto.SignUp signUpDto) {
        verifiedRole(signUpDto.getRole());
        Member member = mapper.toEntity(signUpDto);
        member.setOauthStatus(NORMAL);
        verifyExistsEmail(member.getEmail());
        member.passwordEncoding(passwordEncoder);

        // TODO: 이메일 발송 로직 구현 추가 필요
        Member saveMember = memberRepository.save(member);

        return mapper.toDto(saveMember);
    }

    public MemberDto.Response findMember(String email) {
        Member findMember = findVerifiedMember(email);

        return mapper.toDto(findMember);
    }

    public MemberDto.Response updateMember(MemberDto.Patch patchDto, String email) {
        Member member = mapper.toEntity(patchDto);
        Member findMember = findVerifiedMember(email);
        Member updateMember = beanUtils.copyNonNullProperties(member, findMember);

        return mapper.toDto(updateMember);
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
}
