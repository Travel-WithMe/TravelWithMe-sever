package com.frog.travelwithme.global.security.auth.utils;

import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static com.frog.travelwithme.common.EnumCollection.Roles.ADMIN;
import static com.frog.travelwithme.common.EnumCollection.Roles.USER;

/**
 * CustomAuthorityUtils 설명: DB에 저장된 Role을 기반으로 권한 정보 생성
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
public class CustomAuthorityUtils {
    public static List<GrantedAuthority> createAuthorities(String role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public static void verifiedRole(String role) {
        if (role == null) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ROLE_DOES_NOT_EXISTS);
        } else if (!role.equals(USER.toString()) && !role.equals(ADMIN.toString())) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ROLE_INVALID);
        }
    }
}
