package com.frog.travelwithme.global.security.auth.service;

import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.redis.RedisService;
import com.frog.travelwithme.global.security.auth.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/05
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AES128Config aes128Config;
    private final RedisService redisService;
    private final MemberRepository memberRepository;

    public String reissueAccessToken(String encryptedRefreshToken) {
        this.verifiedRefreshToken(encryptedRefreshToken);
        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisService.getValues(email);

        if (redisService.validateValues(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
            Member findMember = this.findMemberByEmail(email);
            CustomUserDetails userDetails = CustomUserDetails.of(findMember);
            TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
            String newAccessToken = tokenDto.getAccessToken();
            int refreshTokenExpirationMinutes = jwtTokenProvider.getRefreshTokenExpirationMinutes();
            redisService.setValues(refreshToken, newAccessToken,
                    Duration.ofMinutes(refreshTokenExpirationMinutes));
            return newAccessToken;
        } else throw new BusinessLogicException(ExceptionCode.TOKEN_IS_NOT_SAME);
    }

    public void logout(String encryptedRefreshToken, String accessToken) {
        verifiedRefreshToken(encryptedRefreshToken);
        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisService.getValues(email);
        if (!redisService.validateValues(redisRefreshToken)) {
            redisService.deleteValues(email);

            // 로그아웃 시 Access Token Redis 저장 ( key = Access Token / value = "logout" )
            int accessTokenExpirationMinutes = jwtTokenProvider.getAccessTokenExpirationMinutes();
            redisService.setValues(accessToken, "logout", Duration.ofMinutes(accessTokenExpirationMinutes));
        }
    }

    private void verifiedRefreshToken(String encryptedRefreshToken) {
        if (encryptedRefreshToken == null) {
            throw new BusinessLogicException(ExceptionCode.HEADER_REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
