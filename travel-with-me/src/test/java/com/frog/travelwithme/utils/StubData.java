package com.frog.travelwithme.utils;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.SignUp;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * StubData 설명: 테스트를 위한 Stub data 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/03
 **/
public class StubData {
    public static class MockMember {
        static Long id = 1L;
        @Getter
        static String email = "e_ma-il@gmail.com";
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

        public static SignUp getFailedSignUpDtoByEmail(String failedEmail) {
            return SignUp.builder()
                    .email(failedEmail)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .introduction(introduction)
                    .image(image)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static SignUp getFailedSignUpDtoByPassword(String failedPassword) {
            return SignUp.builder()
                    .email(email)
                    .password(failedPassword)
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
                    .password("patch" + password)
                    .nickname("patch" + nickname)
                    .address("patch" + address)
                    .nation("patch" + nation)
                    .image("patch" + image)
                    .introduction("patch" + introduction)
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

    public static class MockBuddy {

        static Long id = 1L;
        static String title = "바하마 배편 동행 구해요";
        static String content = "1인 방예약이 너무비싸 쉐어하실분 구합니다!";
        static String travelNationality = "The Bahamas";
        static String travelStartDate = "2023-01-01"; //LocalDate.of(2023,01,01);
        static String travelEndDate = "2023-01-03"; //LocalDate.of(2023,01,03);
        static Long viewCount = 0L;
        static Long commentCount = 0L;
        static LocalTime localTime = LocalTime.of(0, 0);


        public static BuddyRecruitment getBuddyRecruitment() {
            BuddyRecruitment buddyRecruitment = BuddyRecruitment.builder()
                    .id(id)
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(LocalDateTime.of(LocalDate.parse(travelStartDate),localTime))
                    .travelEndDate(LocalDateTime.of(LocalDate.parse(travelEndDate),localTime))
                    .buddyRecruitmentStatus(EnumCollection.BuddyRecruitmentStatus.IN_PROGRESS)
                    .build();

            return buddyRecruitment;
        }

        public static BuddyDto.PostRecruitment getPostRecruitment() {
            return BuddyDto.PostRecruitment.builder()
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(travelStartDate)
                    .travelEndDate(travelEndDate)
                    .build();
        }

        public static BuddyDto.ResponseRecruitment getResponseRecruitment() {
            return BuddyDto.ResponseRecruitment.builder()
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(LocalDate.parse(travelStartDate))
                    .travelEndDate(LocalDate.parse(travelEndDate))
                    .viewCount(viewCount)
                    .commentCount(commentCount)
                    .nickname(MockMember.nickname)
                    .memberImage(MockMember.image)
                    .build();
        }
    }
}
