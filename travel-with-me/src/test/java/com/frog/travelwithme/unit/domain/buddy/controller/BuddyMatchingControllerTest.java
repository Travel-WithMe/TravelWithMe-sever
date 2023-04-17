package com.frog.travelwithme.unit.domain.buddy.controller;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.BuddyMatchingController;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyMatchingService;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@WebMvcTest(
        controllers = BuddyMatchingController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class BuddyMatchingControllerTest {

    private final String BASE_URL = "/matching";

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected BuddyMatchingService buddyMatchingService;

    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("Test Example Controller")
    @WithMockCustomUser
    void test() throws Exception {
        // given

        // when

        // then

    }

}
