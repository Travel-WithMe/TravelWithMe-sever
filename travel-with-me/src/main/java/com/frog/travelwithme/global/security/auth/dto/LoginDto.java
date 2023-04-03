package com.frog.travelwithme.global.security.auth.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * LoginDto 설명: 로그인 시 전달되는 User 데이터
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/27
 **/
@Data
@Setter(AccessLevel.NONE)
public class LoginDto {
    private String email;
    private String password;
}
