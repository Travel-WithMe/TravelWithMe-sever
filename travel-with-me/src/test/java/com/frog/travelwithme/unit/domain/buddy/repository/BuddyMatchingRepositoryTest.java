package com.frog.travelwithme.unit.domain.buddy.repository;

import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyMatchingRepository;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.config.QuerydslConfig;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.utils.StubData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
@ExtendWith(SpringExtension.class)
class BuddyMatchingRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected BuddyMatchingRepository buddyMatchingRepository;

    @Autowired
    protected BuddyRecruitmentRepository buddyRecruitmentRepository;

    @Autowired
    protected MemberRepository memberRepository;


    @Test
    @DisplayName("동행매칭 레포지토리 저장")
    void BuddyMatchingRepositoryTest1() {
        // given
        Member member = StubData.MockMember.getMember();
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        Member saveMember = memberRepository.save(member);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(saveMember);
        buddyMatching.addBuddyRecruitment(saveBuddyRecruitment);

        // when
        BuddyMatching saveBuddyMatching = buddyMatchingRepository.save(buddyMatching);

        // then
        assertAll(
                () -> assertEquals(saveBuddyMatching.getStatus(), buddyMatching.getStatus()),
                () -> assertEquals(saveBuddyMatching.getMember(), buddyMatching.getMember()),
                () -> assertEquals(saveBuddyMatching.getBuddyRecruitment(), buddyMatching.getBuddyRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 레포지토리 수정")
    void BuddyMatchingRepositoryTest2() {
        // given
        Member member = StubData.MockMember.getMember();
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        Member saveMember = memberRepository.save(member);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(saveMember);
        buddyMatching.addBuddyRecruitment(saveBuddyRecruitment);
        BuddyMatching saveBuddyMatching = buddyMatchingRepository.save(buddyMatching);

        entityManager.clear();
        entityManager.flush();

        BuddyMatching findBuddyMatching = buddyMatchingRepository.findById(saveBuddyMatching.getId()).get();

        // when
        findBuddyMatching.changeStatus(BuddyMatchingStatus.APPROVE);

        // then
        assertThat(findBuddyMatching.getId()).isEqualTo(saveBuddyMatching.getId());
        assertThat(findBuddyMatching.getStatus()) // Expect : APPROVE
                .isNotEqualTo(buddyMatching.getStatus()); // Result : WAIT
    }

    @Test
    @DisplayName("동행매칭 레포지토리 조회")
    void BuddyMatchingRepositoryTest3() {
        // given
        Member member = StubData.MockMember.getMember();
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        Member saveMember = memberRepository.save(member);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(saveMember);
        buddyMatching.addBuddyRecruitment(saveBuddyRecruitment);
        BuddyMatching saveBuddyMatching = buddyMatchingRepository.save(buddyMatching);

        // when
        BuddyMatching findbuddyMatching = buddyMatchingRepository.findById(saveBuddyMatching.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findbuddyMatching.getStatus(), buddyMatching.getStatus()),
                () -> assertEquals(findbuddyMatching.getMember(), buddyMatching.getMember()),
                () -> assertEquals(findbuddyMatching.getBuddyRecruitment(), buddyMatching.getBuddyRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 레포지토리 삭제")
    void BuddyMatchingRepositoryTest4() {
        // given
        Member member = StubData.MockMember.getMember();
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        Member saveMember = memberRepository.save(member);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(saveMember);
        buddyMatching.addBuddyRecruitment(saveBuddyRecruitment);
        BuddyMatching saveBuddyMatching = buddyMatchingRepository.save(buddyMatching);

        // when
        buddyMatchingRepository.delete(saveBuddyMatching);

        Optional<BuddyMatching> findBuddyMatching = buddyMatchingRepository
                .findById(saveBuddyMatching.getId());

        // then
        assertThatThrownBy(() -> findBuddyMatching.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("동행매칭 회원&동행글로 찾기 : Querydsl")
    void BuddyMatchingRepositoryTest5() {
        // given
        Member member = StubData.MockMember.getMember();
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        Member saveMember = memberRepository.save(member);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        BuddyMatching buddyMatching = StubData.MockBuddy.getBuddyMatching();
        buddyMatching.addMember(saveMember);
        buddyMatching.addBuddyRecruitment(saveBuddyRecruitment);
        BuddyMatching saveBuddyMatching = buddyMatchingRepository.save(buddyMatching);

        // when
       BuddyMatching findBuddyMatching = buddyMatchingRepository.findBuddyMatchingByMemberAndBuddyRecruitment(
               saveMember, saveBuddyRecruitment
       ).get();

        // then
        assertAll(
                () -> assertEquals(findBuddyMatching.getId(), saveBuddyMatching.getId()),
                () -> assertEquals(findBuddyMatching.getStatus(), buddyMatching.getStatus()),
                () -> assertEquals(findBuddyMatching.getMember(), buddyMatching.getMember()),
                () -> assertEquals(findBuddyMatching.getBuddyRecruitment(), buddyMatching.getBuddyRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 회원&동행글로 찾기(반환값 없을 경우) : Querydsl")
    void BuddyMatchingRepositoryTest6() {
        // given
        Member member = StubData.MockMember.getMember();
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        Member saveMember = memberRepository.save(member);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        // when
        Optional<BuddyMatching> findBuddyMatching = buddyMatchingRepository.findBuddyMatchingByMemberAndBuddyRecruitment(
                saveMember, saveBuddyRecruitment
        );

        // then
        assertThat(findBuddyMatching.isEmpty()).isEqualTo(true);
        assertThatThrownBy(() -> findBuddyMatching.get()).isInstanceOf(NoSuchElementException.class);
    }

}
