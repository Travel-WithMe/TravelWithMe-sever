package com.frog.travelwithme.utils.security;

import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * WithMockCustomUserSecurityContextFactory 설명: 테스트 환경에서 사용자 인증 정보를 SecurityContext에 저장
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/06
 **/
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        String role = customUser.role();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        CustomUserDetails userDetails = CustomUserDetails.of(customUser.email(), customUser.password(), role);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getEmail(), userDetails.getPassword(), grantedAuthorities);
        authentication.setDetails(customUser.email());
        context.setAuthentication(authentication);

        return context;
    }
}
