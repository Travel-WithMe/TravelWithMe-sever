package com.frog.travelwithme.domain.member.entity;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUp {
        @NotBlank
        @Email
        private String email;
        // TODO: Password 정규식 분리하여 유효성 검사
        @Pattern(message = "최소 8자 및 최대 20자, 대문자 하나 이상, 소문자 하나 이상, 숫자 하나 및 특수 문자 하나 이상",
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")
        private String password;
        @NotBlank(message = "이름은 공백이 아니어야 합니다.")
        private String nickname;
        @NotNull(message = "국가를 입력해야 합니다.")
        private String nation;
        @NotNull(message = "주소를 입력해야 합니다.")
        private String address;
        private String image;
        private String introduction;
        @NotBlank(message = "권한은 공백이 아니어야 합니다.")
        private String role;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SingUpResponse {
        private String nickname;
        private String nation;
        private String address;
        private String image;
        private String introduction;
        private String role;
        private LocalDateTime createdAt;

        @Builder
        public SingUpResponse(Member member) {
            this.nickname = member.getNickname();
            this.nation = member.getNation();
            this.address = member.getAddress();
            this.image = member.getImage();
            this.introduction = member.getIntroduction();
            this.role = member.getRoles().get(0);
            this.createdAt = member.getCreatedAt();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResponse {
        private Long id;
        private String eamil;
        private String nickname;
        private String role;
    }
}
