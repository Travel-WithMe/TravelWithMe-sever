package com.frog.travelwithme.global.security.auth.controller.dto;

import lombok.*;

/**
 * LoginDto 설명: 로그인 시 전달되는 User 데이터
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/27
 **/
public class AuthDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginDto {
        private String email;
        private String password;
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResponse {
        private Long id;
        private String email;
        private String nickname;
        private String role;
    }
}
