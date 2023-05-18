package com.frog.travelwithme.unit.domain.buddy.controller;

import com.frog.travelwithme.domain.buddy.controller.MatchingController;
import com.frog.travelwithme.domain.buddy.service.MatchingService;
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
        controllers = MatchingController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class MatchingControllerTest {

    private final String BASE_URL = "/recruitments";
    private final String SUB_URL = "/matching";

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected MatchingService matchingService;

    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("동행 매칭신청")
    @WithMockCustomUser
    void matchingControllerTest1() throws Exception {
        // given
        EnumCollection.ResponseBody requestMatching = EnumCollection.ResponseBody.NEW_REQUEST_MATCHING;
        given(matchingService.requestMatchingByEmail(any(),any())).willReturn(requestMatching);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + SUB_URL +
                "/" + "request").build().toUri().toString();
        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(requestMatching.getDescription());
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("동행 매칭취소")
    @WithMockCustomUser
    void matchingControllerTest2() throws Exception {
        // given
        EnumCollection.ResponseBody cancelMatching = EnumCollection.ResponseBody.CANCEL_MATCHING;
        given(matchingService.cancelMatchingByEmail(any(),any())).willReturn(cancelMatching);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + SUB_URL +
                "/" + "cancel").build().toUri().toString();
        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(cancelMatching.getDescription());
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("동행 매칭승인")
    @WithMockCustomUser
    void matchingControllerTest3() throws Exception {
        // given
        EnumCollection.ResponseBody approveMatching = EnumCollection.ResponseBody.APPROVE_MATCHING;
        given(matchingService.approveMatchingByEmail(any(),any(),any())).willReturn(approveMatching);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + SUB_URL +
                "/" +  1 + "/" + "approve").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(approveMatching.getDescription());
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("동행 매칭거절")
    @WithMockCustomUser
    void matchingControllerTest4() throws Exception {
        // given
        EnumCollection.ResponseBody approveMatching = EnumCollection.ResponseBody.REJECT_MATCHING;
        given(matchingService.rejectMatchingByEmail(any(),any(),any())).willReturn(approveMatching);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + SUB_URL +
                "/" +  1 + "/" + "reject").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(approveMatching.getDescription());
        actions
                .andExpect(status().isOk());
    }

}
