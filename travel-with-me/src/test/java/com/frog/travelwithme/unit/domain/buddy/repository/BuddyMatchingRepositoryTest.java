package com.frog.travelwithme.unit.domain.buddy.repository;

import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyMatchingRepository;
import com.frog.travelwithme.global.config.QuerydslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@DataJpaTest
@Import(QuerydslConfig.class)
@ExtendWith(SpringExtension.class)
public class BuddyMatchingRepositoryTest {

    @Autowired
    protected BuddyMatchingRepository buddyMatchingRepository;

    @Test
    @DisplayName("Test Example Repository")
    void test() {
        // given

        // when

        // then
    }

}
