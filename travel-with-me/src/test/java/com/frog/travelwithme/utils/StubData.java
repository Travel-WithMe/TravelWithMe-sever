package com.frog.travelwithme.utils;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.SignUp;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;

import java.time.LocalDateTime;

/**
 * StubData 설명: 테스트를 위한 Stub data 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.1
 * 작성일자: 2023/04/03
 **/
public class StubData {
    public static class MockMember {
        static Long id = 1L;
        static String email = "email@gmail.com";
        static String password = "Password1234!";
        static String nickname = "nickname";
        static String image = "image";
        static String address = "address";
        static String introduction = "introduction";
        static String nation = "nation";
        static String role = "USER";
        static LocalDateTime createdAt = LocalDateTime.now();
        static LocalDateTime lastModifiedAt = LocalDateTime.now();

        public static SignUp getSignUpDto() {
            return SignUp.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .introduction(introduction)
                    .image(image)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static LoginDto getLoginSuccessDto() {
            return LoginDto.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

        public static LoginDto getLoginFailDto() {
            return LoginDto.builder()
                    .email("fail@gmail.com")
                    .password(password)
                    .build();
        }

        public static Member getMember() {
            return Member.builder()
                    .id(id)
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .image(image)
                    .address(address)
                    .introduction(introduction)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static MemberDto.Response getResponseDto() {
            return MemberDto.Response.builder()
                    .id(id)
                    .email(email)
                    .nickname(nickname)
                    .address(address)
                    .nation(nation)
                    .introduction(introduction)
                    .image(image)
                    .role(role)
                    .createdAt(createdAt)
                    .lastModifiedAt(lastModifiedAt)
                    .build();
        }

        public static MemberDto.Patch getPatchDto() {
            return MemberDto.Patch.builder()
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .nation(nation)
                    .image(image)
                    .introduction(introduction)
                    .build();
        }

        public static AuthDto.LoginResponse getLoginResponseDto() {
            return AuthDto.LoginResponse.builder()
                    .id(id)
                    .email(email)
                    .nickname(nickname)
                    .role(role)
                    .build();
        }

        public static CustomUserDetails getUserDetails() {
            return CustomUserDetails.of(email, role);
        }
    }
}
