package com.frog.travelwithme.domain.member.entity;

import com.frog.travelwithme.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 설명: 회원 데이터 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/27
 **/
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nation;

    @Column(nullable = false)
    private String address;
    private String image;
    private String introduction;

    @Embedded
    private Coordinate coordinate;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OAuthStatus oauthstatus;

    @Builder
    public Member(MemberDto.SignUp signUpDto) {
        this.email = signUpDto.getEmail();
        this.password = signUpDto.getPassword();
        this.nickname = signUpDto.getNickname();
        this.nation = signUpDto.getNation();
        this.address = signUpDto.getAddress();
        this.image = signUpDto.getImage();
        this.introduction = signUpDto.getIntroduction();
        this.roles.add(signUpDto.getRole());
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void passwordEncoding(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    public void setOauthStatus(OAuthStatus oauthstatus) {
        this.oauthstatus = oauthstatus;
    }

    public enum OAuthStatus {
        NORMAL("일반"),
        OAUTH("소셜");

        @Getter
        private final String status;

        OAuthStatus(String status) {
            this.status = status;
        }
    }
}
