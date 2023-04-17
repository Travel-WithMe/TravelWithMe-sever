package com.frog.travelwithme.unit.domain.member.repository;


import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.config.QuerydslConfig;
import com.frog.travelwithme.utils.StubData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.OAuthStatus.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
@ExtendWith(SpringExtension.class)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRpository;

    @BeforeEach
    void beforeEach() {
        Member member = StubData.MockMember.getMember();
        member.setOauthStatus(NORMAL);
        memberRpository.save(member);
    }

    @AfterEach
    void afterEach() {
        String email = StubData.MockMember.getEmail();
        memberRpository.deleteByEmail(email);
    }

    @Test
    @DisplayName("회원 조회")
    void memberRepositoryTest1() {
        // given
        String email = StubData.MockMember.getEmail();

        // when
        Optional<Member> optionalMember = memberRpository.findByEmail(email);

        // then
        assertNotNull(optionalMember);
    }

    @Test
    @DisplayName("회원 정보 수정 ")
    void memberRepositoryTest2() {
        // given
        MemberDto.Patch patchDto = StubData.MockMember.getPatchDto();
        String email = StubData.MockMember.getEmail();
        Member expectedMember = memberRpository.findByEmail(email).get();
        expectedMember.updateMemberData(patchDto);

        // when
        memberRpository.save(expectedMember);

        // then
        Member updateMember = memberRpository.findByEmail(email).get();
        assertThat(updateMember.getId()).isEqualTo(expectedMember.getId());
        assertThat(updateMember.getNickname()).isEqualTo(expectedMember.getNickname());
        assertThat(updateMember.getAddress()).isEqualTo(expectedMember.getAddress());
        assertThat(updateMember.getImage()).isEqualTo(expectedMember.getImage());
        assertThat(updateMember.getNation()).isEqualTo(expectedMember.getNation());
        assertThat(updateMember.getIntroduction()).isEqualTo(expectedMember.getIntroduction());
    }

    @Test
    @DisplayName("회원 삭제")
    void memberRepositoryTest3() {
        // given
        String email = StubData.MockMember.getEmail();

        // when
        memberRpository.deleteByEmail(email);

        // then
        Optional<Member> optionlMember = memberRpository.findByEmail(email);
        assertThat(optionlMember).isEmpty();
    }
}
