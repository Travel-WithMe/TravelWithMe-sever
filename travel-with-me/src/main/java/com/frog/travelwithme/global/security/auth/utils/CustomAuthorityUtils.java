package com.frog.travelwithme.global.security.auth.utils;

import com.frog.travelwithme.global.security.auth.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CustomAuthorityUtils 설명: DB에 저장된 Role을 기반으로 권한 정보 생성
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
@Component
public class CustomAuthorityUtils {
    private final List<String> USER_ROLES_STRING = List.of("USER");
    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "USER");

    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    public List<String> createRoles(String role) {
        if (role.equals(Roles.ADMIN.toString())) {
            return ADMIN_ROLES_STRING;
        } else if (role.equals(Roles.USER.toString())) {
            return USER_ROLES_STRING;
        }
        return Collections.emptyList();
    }
}
