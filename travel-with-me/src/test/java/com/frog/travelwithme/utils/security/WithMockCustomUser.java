package com.frog.travelwithme.utils.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

/**
 * WithMockCustomUser 설명: 테스트 환경에서 Custom 사용자 인증 정보 생성
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/06
 **/
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String email() default "email";

    String password() default "password";

    String role() default "USER";
}
