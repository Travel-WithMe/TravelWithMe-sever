package com.frog.travelwithme.unit.domain.buddy.repository;

import com.frog.travelwithme.domain.buddy.entity.Buddy;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.BuddyRepository;
import com.frog.travelwithme.domain.recruitment.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.config.QuerydslConfig;
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
class BuddyRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected BuddyRepository buddyRepository;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected MemberRepository memberRepository;


    @Test
    @DisplayName("동행매칭 레포지토리 저장")
    void BuddyMatchingRepositoryTest1() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(saveMember);
        buddy.addRecruitment(saveRecruitment);

        // when
        Buddy saveBuddy = buddyRepository.save(buddy);

        // then
        assertAll(
                () -> assertEquals(saveBuddy.getStatus(), buddy.getStatus()),
                () -> assertEquals(saveBuddy.getMember(), buddy.getMember()),
                () -> assertEquals(saveBuddy.getRecruitment(), buddy.getRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 레포지토리 수정")
    void BuddyMatchingRepositoryTest2() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(saveMember);
        buddy.addRecruitment(saveRecruitment);
        Buddy saveBuddy = buddyRepository.save(buddy);

        entityManager.clear();
        entityManager.flush();

        Buddy findBuddy = buddyRepository.findById(saveBuddy.getId()).get();

        // when
        findBuddy.approve();

        // then
        assertThat(findBuddy.getId()).isEqualTo(saveBuddy.getId());
        assertThat(findBuddy.getStatus()) // Expect : APPROVE
                .isNotEqualTo(buddy.getStatus()); // Result : WAIT
    }

    @Test
    @DisplayName("동행매칭 레포지토리 조회")
    void BuddyMatchingRepositoryTest3() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(saveMember);
        buddy.addRecruitment(saveRecruitment);
        Buddy saveBuddy = buddyRepository.save(buddy);

        // when
        Buddy findBuddy = buddyRepository.findById(saveBuddy.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findBuddy.getStatus(), buddy.getStatus()),
                () -> assertEquals(findBuddy.getMember(), buddy.getMember()),
                () -> assertEquals(findBuddy.getRecruitment(), buddy.getRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 레포지토리 삭제")
    void BuddyMatchingRepositoryTest4() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(saveMember);
        buddy.addRecruitment(saveRecruitment);
        Buddy saveBuddy = buddyRepository.save(buddy);

        // when
        buddyRepository.delete(saveBuddy);

        Optional<Buddy> findBuddy = buddyRepository
                .findById(saveBuddy.getId());

        // then
        assertThatThrownBy(() -> findBuddy.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("동행매칭 회원&동행글로 찾기 : Querydsl")
    void BuddyMatchingRepositoryTest5() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(saveMember);
        buddy.addRecruitment(saveRecruitment);
        Buddy saveBuddy = buddyRepository.save(buddy);

        // when
       Buddy findBuddy = buddyRepository.findBuddyByMemberAndRecruitment(
               saveMember, saveRecruitment
       ).get();

        // then
        assertAll(
                () -> assertEquals(findBuddy.getId(), saveBuddy.getId()),
                () -> assertEquals(findBuddy.getStatus(), buddy.getStatus()),
                () -> assertEquals(findBuddy.getMember(), buddy.getMember()),
                () -> assertEquals(findBuddy.getRecruitment(), buddy.getRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 회원&동행글로 찾기(반환값 없을 경우) : Querydsl")
    void BuddyMatchingRepositoryTest6() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // when
        Optional<Buddy> findBuddy = buddyRepository.findBuddyByMemberAndRecruitment(
                saveMember, saveRecruitment
        );

        // then
        assertThat(findBuddy.isEmpty()).isEqualTo(true);
        assertThatThrownBy(() -> findBuddy.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("동행매칭 id로 찾기(Recruitment Join) : Querydsl")
    void BuddyMatchingRepositoryTest7() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.addMember(saveMember);
        buddy.addRecruitment(saveRecruitment);
        Buddy saveBuddy = buddyRepository.save(buddy);

        entityManager.flush();
        entityManager.clear();
        log.info("1차 캐시 clear");

        // when
        Buddy findBuddy = buddyRepository.findBuddyByIdJoinRecruitment(saveBuddy.getId()).get();
        Recruitment findRecruitment = findBuddy.getRecruitment();

        // then
        assertAll(
                () -> assertEquals(findRecruitment.getId(), saveBuddy.getRecruitment().getId()),
                () -> assertEquals(findRecruitment.getTitle(), saveBuddy.getRecruitment().getTitle()),
                () -> assertEquals(findRecruitment.getContent(), saveBuddy.getRecruitment().getContent())
        );
    }
}
