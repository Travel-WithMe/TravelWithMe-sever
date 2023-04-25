package com.frog.travelwithme.unit.domain.buddy.controller;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.BuddyMatchingController;
import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyMatchingService;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.global.utils.TimeUtils;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("동행 매칭신청")
    @WithMockCustomUser
    void test() throws Exception {
        // given
        EnumCollection.ResponseBody requestMatching = EnumCollection.ResponseBody.NEW_REQUEST_MATCHING;
        given(buddyMatchingService.requestMatching(any(),any())).willReturn(requestMatching);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        EnumCollection.ResponseBody response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                EnumCollection.ResponseBody.class);

        actions
                .andExpect(status().isOk());
        assertThat(response.getName()).isEqualTo(requestMatching.getName());
        assertThat(response.getDescription()).isEqualTo(requestMatching.getDescription());
    }

}
