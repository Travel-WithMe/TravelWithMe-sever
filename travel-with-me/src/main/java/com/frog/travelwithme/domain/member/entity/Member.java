package com.frog.travelwithme.domain.member.entity;

import com.frog.travelwithme.domain.BaseTimeEntity;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.global.enums.EnumCollection.OAuthStatus;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Optional;

/**
 * Member 설명: 회원 데이터 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.1
 * 작성일자: 2023/03/27
 **/
@Entity
@Getter
@Setter
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

    private String role;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OAuthStatus oauthstatus;

    @Builder
    public Member(Long id, String email, String nickname, String password, String nation, String address,
                  String image, String introduction, String role, OAuthStatus oauthstatus) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.nation = nation;
        this.address = address;
        this.image = image;
        this.introduction = introduction;
        this.role = role;
        this.oauthstatus = oauthstatus;
    }

    public void passwordEncoding(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    public void setOauthStatus(OAuthStatus oauthstatus) {
        this.oauthstatus = oauthstatus;
    }

    public void updateMemberData(MemberDto.Patch patchDto) {
        Optional.ofNullable(patchDto.getNickname())
                .ifPresent(updateNickname -> this.nickname = updateNickname);
        Optional.ofNullable(patchDto.getPassword())
                .ifPresent(updatePassword -> this.password = updatePassword);
        Optional.ofNullable(patchDto.getNation())
                .ifPresent(updateNation -> this.nation = updateNation);
        Optional.ofNullable(patchDto.getImage())
                .ifPresent(updateImage -> this.image = updateImage);
        Optional.ofNullable(patchDto.getAddress())
                .ifPresent(updateAddress -> this.address = updateAddress);
        Optional.ofNullable(patchDto.getIntroduction())
                .ifPresent(updateIntroduction -> this.introduction = updateIntroduction);
    }
}
