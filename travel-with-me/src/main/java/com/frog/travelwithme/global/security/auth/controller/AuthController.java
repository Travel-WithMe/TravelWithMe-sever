package com.frog.travelwithme.global.security.auth.controller;

import com.frog.travelwithme.global.dto.SingleResponseDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/05
 **/
@RestController("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PatchMapping("/reissue")
    public ResponseEntity reissue(HttpServletRequest request,
                                  HttpServletResponse response) {
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String newAccessToken = authService.reissueAccessToken(encryptedRefreshToken, response);
        jwtTokenProvider.accessTokenSetHeader(newAccessToken, response);

        return new ResponseEntity<>(new SingleResponseDto<>(
                "The access token was successfully reissued"), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        authService.logout(encryptedRefreshToken, accessToken);

        return new ResponseEntity<>(new SingleResponseDto<>("Logged out successfully"), HttpStatus.NO_CONTENT);
    }
}
