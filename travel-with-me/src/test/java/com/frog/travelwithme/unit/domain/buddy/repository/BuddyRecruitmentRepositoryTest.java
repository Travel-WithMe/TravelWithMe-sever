package com.frog.travelwithme.unit.domain.buddy.repository;

import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyMatching;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
import com.frog.travelwithme.global.config.QuerydslConfig;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.utils.StubData;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.Null;
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
class BuddyRecruitmentRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected BuddyRecruitmentRepository buddyRecruitmentRepository;

    @Test
    @DisplayName("동행 레포지토리 저장")
    void buddyRecruitmentRepositoryTest1() {
        // given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        // when
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        // then
        assertAll(
                () -> assertEquals(saveBuddyRecruitment.getTitle(), buddyRecruitment.getTitle()),
                () -> assertEquals(saveBuddyRecruitment.getContent(), buddyRecruitment.getContent()),
                () -> assertEquals(saveBuddyRecruitment.getTravelNationality(), buddyRecruitment.getTravelNationality())
        );
    }

    @Test
    @DisplayName("동행 레포지토리 수정")
    void buddyRecruitmentRepositoryTest2() {
        // given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        entityManager.clear();
        entityManager.flush();

        BuddyRecruitment findBuddyRecruitment = buddyRecruitmentRepository.findById(saveBuddyRecruitment.getId()).get();

        // when
        findBuddyRecruitment.changeStatus(EnumCollection.BuddyRecruitmentStatus.END);

        // then
        assertThat(findBuddyRecruitment.getId()).isEqualTo(saveBuddyRecruitment.getId());
        assertThat(findBuddyRecruitment.getBuddyRecruitmentStatus()) // Expect : END
                .isNotEqualTo(buddyRecruitment.getBuddyRecruitmentStatus()); // Result : IN_PROGRESS
    }

    @Test
    @DisplayName("동행 레포지토리 조회")
    void buddyRecruitmentRepositoryTest3() {
        // given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        // when
        BuddyRecruitment findBuddyRecruitment = buddyRecruitmentRepository.findById(saveBuddyRecruitment.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findBuddyRecruitment.getTitle(), buddyRecruitment.getTitle()),
                () -> assertEquals(findBuddyRecruitment.getContent(), buddyRecruitment.getContent()),
                () -> assertEquals(findBuddyRecruitment.getTravelNationality(), buddyRecruitment.getTravelNationality())
        );
    }

    @Test
    @DisplayName("동행 레포지토리 삭제")
    void buddyRecruitmentRepositoryTest4() {
        // given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        // when
        buddyRecruitmentRepository.delete(saveBuddyRecruitment);

        Optional<BuddyRecruitment> findBuddyRecruitment = buddyRecruitmentRepository
                .findById(saveBuddyRecruitment.getId());

        // then
        assertThatThrownBy(() -> findBuddyRecruitment.get()).isInstanceOf(NoSuchElementException.class);
    }
}
