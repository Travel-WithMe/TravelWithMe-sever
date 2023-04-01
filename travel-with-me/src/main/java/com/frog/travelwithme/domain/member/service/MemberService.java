package com.frog.travelwithme.domain.member.service;

import com.frog.travelwithme.common.config.AES128Config;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.redis.RedisDao;
import com.frog.travelwithme.global.security.auth.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.global.security.auth.utils.CustomAuthorityUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.frog.travelwithme.domain.member.entity.Member.OAuthStatus.NORMAL;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final JwtTokenProvider jwtTokenProvider;
    private final AES128Config aes128Config;
    private final RedisDao redisDao;

    public Member signUp(Member member) {
        verifyExistsEmail(member.getEmail());
        member.passwordEncoding(passwordEncoder);
        List<String> roles = createRoles(member);
        member.setRoles(roles);
        member.setOauthStatus(NORMAL);
        // TODO: 이메일 발송 로직 구현 추가 필요

        return memberRepository.save(member);
    }

    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        verifiedRefreshToken(encryptedRefreshToken);
        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisDao.getValues(email);

        if (redisDao.validateValues(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
            Member findMember = findVerifiedMember(email);
            CustomUserDetails userDetails = CustomUserDetails.of(findMember);
            TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
            String newAccessToken = tokenDto.getAccessToken();
            int refreshTokenExpirationMinutes = jwtTokenProvider.getRefreshTokenExpirationMinutes();
            redisDao.setValues(refreshToken, newAccessToken,
                    Duration.ofMinutes(refreshTokenExpirationMinutes));
            jwtTokenProvider.accessTokenSetHeader(newAccessToken, response);
        } else throw new BusinessLogicException(ExceptionCode.TOKEN_IS_NOT_SAME);
    }

    public void logout(HttpServletRequest request) {
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        verifiedRefreshToken(encryptedRefreshToken);
        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisDao.getValues(email);
        if (!redisDao.validateValues(redisRefreshToken)) {
            redisDao.deleteValues(email);

            // 로그아웃 시 Access Token Redis 저장 ( key = Access Token / value = "logout" )
            String accessToken = jwtTokenProvider.resolveAccessToken(request);
            int accessTokenExpirationMinutes = jwtTokenProvider.getAccessTokenExpirationMinutes();
            redisDao.setValues(accessToken, "logout", Duration.ofMinutes(accessTokenExpirationMinutes));
        }
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

    private void verifiedRefreshToken(String encryptedRefreshToken) {
        if (encryptedRefreshToken == null) {
            throw new BusinessLogicException(ExceptionCode.HEADER_REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

    private List<String> createRoles(Member member) {
        List<String> roles = authorityUtils.createRoles(member.getRoles().get(0));
        if (roles.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ROLE_DOES_NOT_EXISTS);
        }

        return roles;
    }
}
