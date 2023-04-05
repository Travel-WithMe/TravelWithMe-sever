package com.frog.travelwithme.global.security.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frog.travelwithme.common.config.AES128Config;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.redis.RedisService;
import com.frog.travelwithme.global.security.auth.dto.LoginDto;
import com.frog.travelwithme.global.security.auth.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.global.security.auth.utils.Responder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

/**
 * JwtAuthenticationFilter 설명: 로그인 검증 및 JWT 발급
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/28
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AES128Config aes128Config;
    private final MemberService memberService;
    private final RedisService redisService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();

        // ServletInputStream을 LoginDto 객체로 역직렬화
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(customUserDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        jwtTokenProvider.accessTokenSetHeader(accessToken, response);
        jwtTokenProvider.refresshTokenSetHeader(encryptedRefreshToken, response);
        Member findMember = memberService.findVerifiedMember(customUserDetails.getId());
        Responder.loginSuccessResponse(response, findMember);

        // 로그인 성공시 Refresh Token Redis 저장 ( key = Email / value = Refresh Token )
        int refreshTokenExpirationMinutes = jwtTokenProvider.getRefreshTokenExpirationMinutes();
        redisService.setValues(findMember.getEmail(), refreshToken, Duration.ofMinutes(refreshTokenExpirationMinutes));

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
