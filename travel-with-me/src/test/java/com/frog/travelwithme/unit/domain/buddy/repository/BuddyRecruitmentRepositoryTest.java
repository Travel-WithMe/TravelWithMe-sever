package com.frog.travelwithme.unit.domain.buddy.repository;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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
@Import(QuerydslConfig.class)
@ExtendWith(SpringExtension.class)
class BuddyRecruitmentRepositoryTest {

    @Autowired
    protected BuddyRecruitmentRepository buddyRecruitmentRepository;

    @Test
    @DisplayName("Create Test")
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
    @DisplayName("Update Test")
    void buddyRecruitmentRepositoryTest2() {
        // given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        // when
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);
        saveBuddyRecruitment.changeStatus(EnumCollection.BuddyRecruitmentStatus.COMPLETE);

        BuddyRecruitment findBuddyRecruitment = buddyRecruitmentRepository.findById(saveBuddyRecruitment.getId()).get();

        // then
        assertThat(findBuddyRecruitment.getId()).isEqualTo(saveBuddyRecruitment.getId());
        assertThat(findBuddyRecruitment.getBuddyRecruitmentStatus()) // Expect : IN_PROGRESS
                .isNotEqualTo(buddyRecruitment.getBuddyRecruitmentStatus()); // Result : COMPLETE
    }

    @Test
    @DisplayName("Read Test")
    void buddyRecruitmentRepositoryTest3() {
        // given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        // when
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);
        BuddyRecruitment findBuddyRecruitment = buddyRecruitmentRepository.findById(saveBuddyRecruitment.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findBuddyRecruitment.getTitle(), buddyRecruitment.getTitle()),
                () -> assertEquals(findBuddyRecruitment.getContent(), buddyRecruitment.getContent()),
                () -> assertEquals(findBuddyRecruitment.getTravelNationality(), buddyRecruitment.getTravelNationality())
        );
    }

    @Test
    @DisplayName("Delete Test")
    void buddyRecruitmentRepositoryTest4() {
        // given
        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();

        // when
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);
        buddyRecruitmentRepository.delete(saveBuddyRecruitment);

        Optional<BuddyRecruitment> findBuddyRecruitment = buddyRecruitmentRepository
                .findById(saveBuddyRecruitment.getId());

        // then
        assertThatThrownBy(() -> findBuddyRecruitment.get()).isInstanceOf(NoSuchElementException.class);
    }
}