package com.frog.travelwithme.domain.member.service;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.EmailVerificationResult;
import com.frog.travelwithme.domain.member.entity.Interest;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.mapper.MemberMapper;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.file.FileUploadService;
import com.frog.travelwithme.global.mail.MailService;
import com.frog.travelwithme.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.frog.travelwithme.global.enums.EnumCollection.AwsS3Path.PROFILEIMAGE;
import static com.frog.travelwithme.global.enums.EnumCollection.OAuthStatus.NORMAL;
import static com.frog.travelwithme.global.security.auth.utils.CustomAuthorityUtils.verifiedRole;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MemberMapper memberMapper;

    private final MailService mailService;

    private final RedisService redisService;

    private final FileUploadService fileUploadService;

    private final InterestService interestService;

    private final FollowService followService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public MemberDto.Response signUp(MemberDto.SignUp signUpDto) {
        verifiedRole(signUpDto.getRole());
        this.checkDuplicatedEmail(signUpDto.getEmail());
        List<Interest> interests = interestService
                .findInterests(Optional.ofNullable(signUpDto.getInterests()).orElse(Collections.emptyList()));
        Member member = memberMapper.toEntity(signUpDto);
        member.setOauthStatus(NORMAL);
        member.passwordEncoding(passwordEncoder);
        member.changeInterests(interests);

        return memberMapper.toDto(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberDto.Response findMemberByEmail(String email) {
        Member findMember = this.findMember(email);

        return memberMapper.toDto(findMember);
    }

    @Transactional(readOnly = true)
    public MemberDto.Response findMemberById(Long id) {
        Member findMember = this.findMember(id);

        return memberMapper.toDto(findMember);
    }

    public MemberDto.Response updateMember(MemberDto.Patch patchDto, String email) {
        Member findMember = this.findMember(email);
        findMember.updateMemberData(patchDto);
        List<Interest> newInterests = interestService
                .findInterests(Optional.ofNullable(patchDto.getInterests()).orElse(Collections.emptyList()));
        findMember.changeInterests(newInterests);

        return memberMapper.toDto(findMember);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("MemberServiceImpl.findMemberAndCheckMemberExists exception occur id: {}", id);
                    throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
                });
    }

    public void deleteMember(String email) {
        Member member = this.findMember(email);
        fileUploadService.remove(member.getImage());
        memberRepository.deleteByEmail(email);
    }

    public MemberDto.Response changeProfileImage(@RequestPart MultipartFile file, String email) {
        Member findMember = this.findMember(email);
        String beforeImageUrl = findMember.getImage();
        String newImageUrl = fileUploadService.upload(file, PROFILEIMAGE);
        findMember.changeImage(newImageUrl);
        fileUploadService.remove(beforeImageUrl);
        return memberMapper.toDto(findMember);
    }

    public MemberDto.Response removeProfileImage(String email) {
        Member member = this.findMember(email);
        String currentProfileImageUrl = member.getImage();
        member.changeImage("defaultImageUrl");
        fileUploadService.remove(currentProfileImageUrl);

        return memberMapper.toDto(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.debug("MemberServiceImpl.findMemberAndCheckMemberExists exception occur email: {}", email);
                    return new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
                });
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "Travel with me 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    public EmailVerificationResult verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        return EmailVerificationResult.from(authResult);
    }

    public void follow(String followerEmail, String followeeEmail) {
        Member follower = this.findMember(followerEmail);
        Member followee = this.findMember(followeeEmail);
        followService.follow(follower, followee);
    }

    public void unfollow(String followerEmail, String followeeEmail) {
        Member follower = this.findMember(followerEmail);
        Member followee = this.findMember(followeeEmail);
        followService.unfollow(follower, followee);
    }

    public void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    public void checkDuplicatedNickname(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", nickname);
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

    private void uploadAndAndChangeImage(MultipartFile multipartFile,
                                         Member saveMember) {
        if (multipartFile != null) {
            String imageUrl = fileUploadService.upload(multipartFile, PROFILEIMAGE);
            saveMember.changeImage(imageUrl);
        }
    }
}