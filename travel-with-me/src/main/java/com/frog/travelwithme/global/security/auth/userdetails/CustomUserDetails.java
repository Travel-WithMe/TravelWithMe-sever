package com.frog.travelwithme.global.security.auth.userdetails;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.global.security.auth.utils.CustomAuthorityUtils;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * CustomUserDetails 설명: Spring Security에서 관리하는 User 정보 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/28
 **/
@Getter
public class CustomUserDetails extends Member implements UserDetails {
    private final Long id;
    private final String email;
    private final List<String> roles;
    private String password;

    private CustomUserDetails(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.roles = member.getRoles();
    }

    private CustomUserDetails(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public static CustomUserDetails of(Member member) {
        return new CustomUserDetails(member);
    }

    public static CustomUserDetails of(Long id, String email, List<String> roles) {
        return new CustomUserDetails(id, email, roles);
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        CustomAuthorityUtils authorityUtils = new CustomAuthorityUtils();
        return authorityUtils.createAuthorities(roles);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
