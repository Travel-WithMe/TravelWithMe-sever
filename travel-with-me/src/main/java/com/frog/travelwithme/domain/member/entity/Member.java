package com.frog.travelwithme.domain.member.entity;

import com.frog.travelwithme.domain.common.BaseTimeEntity;
import com.frog.travelwithme.domain.feed.entity.Feed;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.global.enums.EnumCollection.Gender;
import com.frog.travelwithme.global.enums.EnumCollection.Nation;
import com.frog.travelwithme.global.enums.EnumCollection.OAuthStatus;
import io.jsonwebtoken.lang.Collections;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Member 설명: 회원 데이터 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/27
 **/
@Entity
@Getter
@DynamicInsert
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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Nation nation;

    @Column(nullable = false)
    private String address;

    private String image;

    private String introduction;

    @Embedded
    private Coordinate coordinate;

    private String role;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OAuthStatus oauthstatus;

    @ManyToMany(mappedBy = "likedMembers")
    private List<Feed> likedFeeds = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "member_interest",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    private List<Interest> interests = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String nickname, String password, Gender gender, Nation nation,
                  String address, String introduction, String role, OAuthStatus oauthstatus, String image) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.gender = gender;
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
        Optional.ofNullable(patchDto.getGender())
                .ifPresent(updateGender -> this.gender = patchDto.getGender());
        Optional.ofNullable(patchDto.getAddress())
                .ifPresent(updateAddress -> this.address = updateAddress);
        Optional.ofNullable(patchDto.getIntroduction())
                .ifPresent(updateIntroduction -> this.introduction = updateIntroduction);
    }

    public void changeImage(String newImage) {
        this.image = newImage;
    }

    public void changeInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public void addFollowing(Follow follow) {
        if (this.followings == null) {
            this.followings = new ArrayList<>();
        }
        this.followings.add(follow);
    }

    public void removeFollowing(Follow follow) {
        if (!Collections.isEmpty(this.followings)) {
            this.followings.remove(follow);
        }
    }

    public void addFollower(Follow follow) {
        if (this.followers == null) {
            this.followers = new ArrayList<>();
        }
        this.followers.add(follow);
    }

    public void removeFollower(Follow follow) {
        if (!Collections.isEmpty(this.followers)) {
            this.followers.remove(follow);
        }
    }
}
