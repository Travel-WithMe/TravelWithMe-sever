package com.frog.travelwithme.domain.member.service;

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

import static com.frog.travelwithme.global.enums.EnumCollection.OAuthStatus.NORMAL;
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
    private final MemberMapper mapper;

    public MemberDto.Response signUp(MemberDto.SignUp signUpDto) {
        verifiedRole(signUpDto.getRole());
        Member member = mapper.toEntity(signUpDto);
        member.setOauthStatus(NORMAL);
        this.checkDuplicatedEmail(member.getEmail());
        member.passwordEncoding(passwordEncoder);

        // TODO: 이메일 발송 로직 구현 추가 필요
        Member saveMember = memberRepository.save(member);

        return mapper.toDto(saveMember);
    }

    @Transactional(readOnly = true)
    public MemberDto.Response findMemberByEmail(String email) {
        Member findMember = this.findMemberAndCheckMemberExists(email);

        return mapper.toDto(findMember);
    }

    @Transactional(readOnly = true)
    public MemberDto.Response findMemberById(Long id) {
        Member findMember = this.findMemberAndCheckMemberExists(id);

        return mapper.toDto(findMember);
    }

    public MemberDto.Response updateMember(MemberDto.Patch patchDto, String email) {
        Member findMember = this.findMemberAndCheckMemberExists(email);
        findMember.updateMemberData(patchDto);

        return mapper.toDto(findMember);
    }

    @Transactional(readOnly = true)
    public Member findMemberAndCheckMemberExists(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberAndCheckMemberExists(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    public void deleteMember(String email) {
        this.findMemberAndCheckMemberExists(email);
        memberRepository.deleteByEmail(email);
    }
}
