package com.frog.travelwithme.utils;

import com.frog.travelwithme.domain.buddyrecuirtment.common.DeletionEntity;
import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.EmailVerificationResult;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.SignUp;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.enums.EnumCollection.Gender;
import com.frog.travelwithme.global.enums.EnumCollection.BuddyMatchingStatus;
import com.frog.travelwithme.global.enums.EnumCollection.OAuthStatus;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.global.utils.TimeUtils;
import lombok.Getter;

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
        static final Long id = 1L;
        @Getter
        static String email = "e_ma-il@gmail.com";
        static String password = "Password1234!";
        static String nickname = "nickname";
        @Getter
        static String image = "defaultImageUrl";
        static String address = "address";
        static String introduction = "introduction";
        static String nation = "nation";
        static String role = "USER";
        static Gender enumGender = Gender.MALE;
        static String stringGender = enumGender.getDescription();
        static String patchStringGender = Gender.FEMALE.getDescription();
        static LocalDateTime createdAt = LocalDateTime.now();
        static LocalDateTime lastModifiedAt = LocalDateTime.now();
        @Getter
        static String emailKey = "email";
        @Getter
        static String codeKey = "code";
        @Getter
        static String codeValue = "123456";
        @Getter
        static String authCodePrefix = "AuthCode ";

        static String emailOther = "emailOther@gmail.com";
        static String nicknameOther = "nicknameOther";

        public static SignUp getSignUpDto() {
            return SignUp.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .introduction(introduction)
                    .nation(nation)
                    .gender(stringGender)
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
                    .gender(stringGender)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static SignUp getSignUpDtoByEmailAndNickname(String email, String nickname) {
            return SignUp.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .introduction(introduction)
                    .gender(stringGender)
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
                    .gender(stringGender)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static SignUp getFailedSignUpDtoByGender(String failedGender) {
            return SignUp.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .introduction(introduction)
                    .gender(failedGender)
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

        public static Member getMemberByEmailAndNickname(String email, String nickname) {
            return Member.builder()
                    .id(id)
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .gender(enumGender)
                    .address(address)
                    .introduction(introduction)
                    .nation(nation)
                    .role(role)
                    .build();
        }

        public static Member getMember() {
            return Member.builder()
                    .id(id)
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .gender(enumGender)
                    .address(address)
                    .introduction(introduction)
                    .nation(nation)
                    .role(role)
                    .oauthstatus(OAuthStatus.NORMAL)
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
                    .gender(stringGender)
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
                    .gender(patchStringGender)
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

        public static CustomUserDetails getUserDetailsByEmailAndRole(String email, String role) {
            return CustomUserDetails.of(email, role);
        }

        public static EmailVerificationResult getEmailVerificationResult(boolean authResult) {
            return EmailVerificationResult.from(authResult);
        }
    }

    public static class MockBuddy {

        // 1번 Mock BuddyRecruitment 정보
        static Long id = 1L;
        static String title = "바하마 배편 동행 구해요";
        static String content = "1인 방예약이 너무비싸 쉐어하실분 구합니다!";
        static String travelNationality = "The Bahamas";
        static String travelStartDate = "2023-01-01";
        static String travelEndDate = "2023-01-03";
        static Long viewCount = 0L;
        static Long commentCount = 0L;

        // 2번 Mock BuddyRecruitment 정보
        static String patchTitle = "페루여행 쿠스코에서 콜릭티보 동행";
        static String patchContent = "콜렉티보 흥정이랑 같이 마추픽추까지 이동하실분 구해요!";
        static String patchTravelNationality = "Peru";
        static String patchTravelStartDate = "2023-01-30";
        static String patchTravelEndDate = "2023-01-31";


        public static BuddyRecruitment getBuddyRecruitment() {
            return BuddyRecruitment.builder()
                    .id(id)
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(TimeUtils.stringToLocalDateTime(travelStartDate))
                    .travelEndDate(TimeUtils.stringToLocalDateTime(travelEndDate))
                    .buddyRecruitmentStatus(EnumCollection.BuddyRecruitmentStatus.IN_PROGRESS)
                    .deletionEntity(new DeletionEntity())
                    .build();
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

        public static BuddyDto.PatchRecruitment getPatchRecruitment() {
            return BuddyDto.PatchRecruitment.builder()
                    .title(patchTitle)
                    .content(patchContent)
                    .travelNationality(patchTravelNationality)
                    .travelStartDate(patchTravelStartDate)
                    .travelEndDate(patchTravelEndDate)
                    .build();
        }

        public static BuddyDto.PostResponseRecruitment getPostResponseRecruitment() {
            return BuddyDto.PostResponseRecruitment.builder()
                    .title(title)
                    .content(content)
                    .travelNationality(travelNationality)
                    .travelStartDate(TimeUtils.stringToLocalDate(travelStartDate))
                    .travelEndDate(TimeUtils.stringToLocalDate(travelEndDate))
                    .viewCount(viewCount)
                    .commentCount(commentCount)
                    .nickname(MockMember.nickname)
                    .memberImage(MockMember.image)
                    .build();
        }

        public static BuddyDto.PatchResponseRecruitment getPatchResponseRecruitment() {
            return BuddyDto.PatchResponseRecruitment.builder()
                    .title(patchTitle)
                    .content(patchContent)
                    .travelNationality(patchTravelNationality)
                    .travelStartDate(TimeUtils.stringToLocalDate(patchTravelStartDate))
                    .travelEndDate(TimeUtils.stringToLocalDate(patchTravelEndDate))
                    .build();
        }

        public static BuddyMatching getBuddyMatching() {
            return BuddyMatching.builder()
                    .status(BuddyMatchingStatus.WAIT)
                    .build();
        }
    }
}
