package com.frog.travelwithme.unit.domain.buddy.repository;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
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

import static org.assertj.core.api.Assertions.*;
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
class RecruitmentRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Test
    @DisplayName("동행 레포지토리 저장")
    void buddyRecruitmentRepositoryTest1() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        // when
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // then
        assertAll(
                () -> assertEquals(saveRecruitment.getTitle(), recruitment.getTitle()),
                () -> assertEquals(saveRecruitment.getContent(), recruitment.getContent()),
                () -> assertEquals(saveRecruitment.getTravelNationality(), recruitment.getTravelNationality())
        );
    }

    @Test
    @DisplayName("동행 레포지토리 수정")
    void buddyRecruitmentRepositoryTest2() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        entityManager.clear();
        entityManager.flush();

        Recruitment findRecruitment = recruitmentRepository.findById(saveRecruitment.getId()).get();

        // when
        findRecruitment.end();

        // then
        assertThat(findRecruitment.getId()).isEqualTo(saveRecruitment.getId());
        assertThat(findRecruitment.getRecruitmentStatus()) // Expect : END
                .isNotEqualTo(recruitment.getRecruitmentStatus()); // Result : IN_PROGRESS
    }

    @Test
    @DisplayName("동행 레포지토리 조회")
    void buddyRecruitmentRepositoryTest3() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // when
        Recruitment findRecruitment = recruitmentRepository.findById(saveRecruitment.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findRecruitment.getTitle(), recruitment.getTitle()),
                () -> assertEquals(findRecruitment.getContent(), recruitment.getContent()),
                () -> assertEquals(findRecruitment.getTravelNationality(), recruitment.getTravelNationality())
        );
    }

    @Test
    @DisplayName("동행 레포지토리 삭제")
    void buddyRecruitmentRepositoryTest4() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // when
        recruitmentRepository.delete(saveRecruitment);

        Optional<Recruitment> findRecruitment = recruitmentRepository
                .findById(saveRecruitment.getId());

        // then
        assertThatThrownBy(() -> findRecruitment.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("동행글 id 조회(Member Join) : Querydsl")
    void buddyRecruitmentRepositoryTest5() {
        // given
        Member member = StubData.MockMember.getMember();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member saveMember = memberRepository.save(member);
        recruitment.addMember(saveMember);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        entityManager.flush();
        entityManager.clear();
        log.info("1차 캐시 clear");

        // when
        Recruitment findRecruitment = recruitmentRepository.findRecruitmentByIdJoinMember(saveRecruitment.getId()).get();
        Member findMember = findRecruitment.getMember();

        // then
        assertAll(
                () -> assertEquals(findMember.getEmail(), recruitment.getMember().getEmail()),
                () -> assertEquals(findMember.getNickname(), recruitment.getMember().getNickname())
        );
    }
}
