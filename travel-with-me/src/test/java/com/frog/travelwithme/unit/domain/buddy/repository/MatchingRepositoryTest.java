package com.frog.travelwithme.unit.domain.recruitment.repository;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.MatchingRepository;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
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
class MatchingRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected MatchingRepository matchingRepository;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected MemberRepository memberRepository;


    @Test
    @DisplayName("동행매칭 레포지토리 저장")
    void matchingRepositoryTest1() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(saveMember);
        matching.addRecruitment(saveRecruitment);

        // when
        Matching saveMatching = matchingRepository.save(matching);

        // then
        assertAll(
                () -> assertEquals(saveMatching.getStatus(), matching.getStatus()),
                () -> assertEquals(saveMatching.getMember(), matching.getMember()),
                () -> assertEquals(saveMatching.getRecruitment(), matching.getRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 레포지토리 수정")
    void matchingRepositoryTest2() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(saveMember);
        matching.addRecruitment(saveRecruitment);
        Matching saveMatching = matchingRepository.save(matching);

        entityManager.clear();
        entityManager.flush();

        Matching findMatching = matchingRepository.findById(saveMatching.getId()).get();

        // when
        findMatching.approve();

        // then
        assertThat(findMatching.getId()).isEqualTo(saveMatching.getId());
        assertThat(findMatching.getStatus()) // Expect : APPROVE
                .isNotEqualTo(matching.getStatus()); // Result : WAIT
    }

    @Test
    @DisplayName("동행매칭 레포지토리 조회")
    void matchingRepositoryTest3() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(saveMember);
        matching.addRecruitment(saveRecruitment);
        Matching saveMatching = matchingRepository.save(matching);

        // when
        Matching findMatching = matchingRepository.findById(saveMatching.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findMatching.getStatus(), matching.getStatus()),
                () -> assertEquals(findMatching.getMember(), matching.getMember()),
                () -> assertEquals(findMatching.getRecruitment(), matching.getRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 레포지토리 삭제")
    void matchingRepositoryTest4() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(saveMember);
        matching.addRecruitment(saveRecruitment);
        Matching saveMatching = matchingRepository.save(matching);

        // when
        matchingRepository.delete(saveMatching);

        Optional<Matching> findMatching = matchingRepository
                .findById(saveMatching.getId());

        // then
        assertThatThrownBy(() -> findMatching.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("동행매칭 회원&동행글로 찾기 : Querydsl")
    void matchingRepositoryTest5() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(saveMember);
        matching.addRecruitment(saveRecruitment);
        Matching saveMatching = matchingRepository.save(matching);

        // when
       Matching findMatching = matchingRepository.findMatchingByMemberAndRecruitment(
               saveMember, saveRecruitment
       ).get();

        // then
        assertAll(
                () -> assertEquals(findMatching.getId(), saveMatching.getId()),
                () -> assertEquals(findMatching.getStatus(), matching.getStatus()),
                () -> assertEquals(findMatching.getMember(), matching.getMember()),
                () -> assertEquals(findMatching.getRecruitment(), matching.getRecruitment())
        );
    }

    @Test
    @DisplayName("동행매칭 회원&동행글로 찾기(반환값 없을 경우) : Querydsl")
    void matchingRepositoryTest6() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // when
        Optional<Matching> findMatching = matchingRepository.findMatchingByMemberAndRecruitment(
                saveMember, saveRecruitment
        );

        // then
        assertThat(findMatching.isEmpty()).isEqualTo(true);
        assertThatThrownBy(() -> findMatching.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("동행매칭 id로 찾기(Recruitment Join) : Querydsl")
    void matchingRepositoryTest7() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        Member saveMember = memberRepository.save(member);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(saveMember);
        matching.addRecruitment(saveRecruitment);
        Matching saveMatching = matchingRepository.save(matching);

        entityManager.flush();
        entityManager.clear();
        log.info("1차 캐시 clear");

        // when
        Matching findMatching = matchingRepository.findMatchingById(saveMatching.getId()).get();
        Recruitment findRecruitment = findMatching.getRecruitment();

        // then
        assertAll(
                () -> assertEquals(findRecruitment.getId(), saveMatching.getRecruitment().getId()),
                () -> assertEquals(findRecruitment.getTitle(), saveMatching.getRecruitment().getTitle()),
                () -> assertEquals(findRecruitment.getContent(), saveMatching.getRecruitment().getContent())
        );
    }
}
