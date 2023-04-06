package com.frog.travelwithme.utils.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * WithMockCustomUser 설명: 테스트 환경에서 Admin 권한의 Custom 사용자 인증 정보 생성
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/06
 **/
@Retention(RetentionPolicy.RUNTIME)
@WithMockCustomUser(role = "ADMIN")
public @interface WithMockCustomAdmin {
}
