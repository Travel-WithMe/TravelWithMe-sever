package com.frog.travelwithme.unit.domain.buddy.service;

import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyMatchingServiceImpl;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@ExtendWith(MockitoExtension.class)
class BuddyMatchingServiceTest {

    @InjectMocks
    protected BuddyMatchingServiceImpl buddyMatchingService;

    @Mock
    protected BuddyRecruitmentService buddyRecruitmentService;

    @Test
    @DisplayName("Test Example Service")
    void test() {

    }

}
