package com.frog.travelwithme.unit.domain.recruitment.repository;

import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.recruitment.repository.RecruitmentRepository;
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

        Recruitment findBuddy = recruitmentRepository.findById(saveRecruitment.getId()).get();

        // when
        findBuddy.changeStatus(EnumCollection.RecruitmentStatus.END);

        // then
        assertThat(findBuddy.getId()).isEqualTo(saveRecruitment.getId());
        assertThat(findBuddy.getRecruitmentStatus()) // Expect : END
                .isNotEqualTo(recruitment.getRecruitmentStatus()); // Result : IN_PROGRESS
    }

    @DisplayName("동행 레포지토리 조회")
    void buddyRecruitmentRepositoryTest3() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // when
        Recruitment findBuddy = recruitmentRepository.findById(saveRecruitment.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findBuddy.getTitle(), recruitment.getTitle()),
                () -> assertEquals(findBuddy.getContent(), recruitment.getContent()),
                () -> assertEquals(findBuddy.getTravelNationality(), recruitment.getTravelNationality())
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

        Optional<Recruitment> findBuddy = recruitmentRepository
                .findById(saveRecruitment.getId());

        // then
        assertThatThrownBy(() -> findBuddy.get()).isInstanceOf(NoSuchElementException.class);
    }
}
