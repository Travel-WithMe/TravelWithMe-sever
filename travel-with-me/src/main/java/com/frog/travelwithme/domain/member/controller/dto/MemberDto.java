package com.frog.travelwithme.domain.member.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frog.travelwithme.global.enums.EnumCollection.Gender;
import com.frog.travelwithme.global.enums.EnumCollection.Nation;
import com.frog.travelwithme.global.validation.CustomAnnotationCollection.CustomEmail;
import com.frog.travelwithme.global.validation.CustomAnnotationCollection.Password;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
public class MemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUp {
        @NotBlank
        @CustomEmail
        private String email;
        // TODO: Password 정규식 분리하여 유효성 검사
        @Password
        private String password;
        @NotBlank(message = "이름은 공백이 아니어야 합니다.")
        private String nickname;
        @NotNull(message = "성별을 선택해야 합니다.")
        private Gender gender;
        @NotNull(message = "국가를 선택해야 합니다.")
        private Nation nation;
        @NotNull(message = "주소를 입력해야 합니다.")
        private String address;
        private String introduction;
        @NotBlank(message = "권한은 공백이 아니어야 합니다.")
        private String role;
        private List<String> interests;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        private String password;
        private String nickname;
        private Gender gender;
        private Nation nation;
        private String address;
        private String introduction;
        private List<String> interests;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long id;
        private String email;
        private String nickname;
        private Nation nation;
        private Gender gender;
        private String image;
        private String address;
        private String introduction;
        private String role;
        private Long followerCount;
        private Long followingCount;
        @JsonProperty("isFollow")
        private boolean follow;
        private List<String> interests;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EmailVerificationResult {
        private boolean success;

        public static EmailVerificationResult from(boolean authResult) {
            return new EmailVerificationResult(authResult);
        }
    }
}
