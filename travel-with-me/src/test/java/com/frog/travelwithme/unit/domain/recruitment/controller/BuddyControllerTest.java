package com.frog.travelwithme.unit.domain.recruitment.controller;

import com.frog.travelwithme.domain.recruitment.controller.BuddyController;
import com.frog.travelwithme.domain.recruitment.service.BuddyService;
import com.frog.travelwithme.global.dto.MessageResponseDto;
import com.frog.travelwithme.global.enums.EnumCollection;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
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
        controllers = BuddyController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class BuddyControllerTest {

    private final String BASE_URL = "/recruitments";
    private final String SUB_URL = "/buddy";

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected BuddyService buddyService;

    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("동행 매칭신청")
    @WithMockCustomUser
    void buddyMatchingControllerTest1() throws Exception {
        // given
        EnumCollection.ResponseBody requestBuddy = EnumCollection.ResponseBody.NEW_REQUEST_BUDDY;
        given(buddyService.requestBuddyByEmail(any(),any())).willReturn(requestBuddy);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + SUB_URL +
                "/" + "request").build().toUri().toString();
        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(requestBuddy.getDescription());
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("동행 매칭취소")
    @WithMockCustomUser
    void buddyMatchingControllerTest2() throws Exception {
        // given
        EnumCollection.ResponseBody cancelBuddy = EnumCollection.ResponseBody.CANCEL_BUDDY;
        given(buddyService.cancelBuddyByEmail(any(),any())).willReturn(cancelBuddy);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + SUB_URL +
                "/" + "cancel").build().toUri().toString();
        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(cancelBuddy.getDescription());
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("동행 매칭승인")
    @WithMockCustomUser
    void buddyMatchingControllerTest3() throws Exception {
        // given
        EnumCollection.ResponseBody approveBuddy = EnumCollection.ResponseBody.APPROVE_BUDDY;
        given(buddyService.approveBuddyByEmail(any(),any(),any())).willReturn(approveBuddy);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + SUB_URL +
                "/" +  1 + "/" + "approve").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(approveBuddy.getDescription());
        actions
                .andExpect(status().isOk());
    }

}
