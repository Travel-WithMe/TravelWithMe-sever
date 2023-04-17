package com.frog.travelwithme.global.security.auth.utils;

import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static com.frog.travelwithme.global.enums.EnumCollection.Roles.ADMIN;
import static com.frog.travelwithme.global.enums.EnumCollection.Roles.USER;

/**
 * CustomAuthorityUtils 설명: DB에 저장된 Role을 기반으로 권한 정보 생성
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
@Slf4j
public class CustomAuthorityUtils {
    public static List<GrantedAuthority> createAuthorities(String role) {
        log.info("CustomAuthorityUtils.createAuthorities excute, role = {}", role);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public static void verifiedRole(String role) {
        log.info("CustomAuthorityUtils.verifiedRole excute, role = {}", role);
        if (role == null) {
            log.debug("CustomAuthorityUtils.verifiedRole exception occur role: {}", (Object) null);
            throw new BusinessLogicException(ExceptionCode.MEMBER_ROLE_DOES_NOT_EXISTS);
        } else if (!role.equals(USER.toString()) && !role.equals(ADMIN.toString())) {
            log.debug("CustomAuthorityUtils.verifiedRole exception occur role: {}", role);
            throw new BusinessLogicException(ExceptionCode.MEMBER_ROLE_INVALID);
        }
    }
}
