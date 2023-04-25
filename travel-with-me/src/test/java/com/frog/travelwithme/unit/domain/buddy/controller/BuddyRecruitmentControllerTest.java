package com.frog.travelwithme.unit.domain.buddy.controller;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.BuddyRecruitmentController;
import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentService;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.global.utils.TimeUtils;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@WebMvcTest(
        controllers = {
                BuddyRecruitmentController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class BuddyRecruitmentControllerTest {

    private final String BASE_URL = "/recruitments";

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected BuddyRecruitmentService buddyRecruitmentService;

    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("동행 모집글 작성")
    @WithMockCustomUser
    void buddyRecruitmentControllerTest1() throws Exception {
        // given
        BuddyDto.PostRecruitment postRecruitmentDto = StubData.MockBuddy.getPostRecruitment();
        BuddyDto.PostResponseRecruitment responseRecruitmentDto = StubData.MockBuddy.getPostResponseRecruitment();

        given(buddyRecruitmentService.createBuddyRecruitment(any(),any())).willReturn(responseRecruitmentDto);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();

        String json = ObjectMapperUtils.dtoToJsonString(postRecruitmentDto);
        ResultActions actions = ResultActionsUtils.postRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        BuddyDto.PostResponseRecruitment response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                BuddyDto.PostResponseRecruitment.class);

        actions
                .andExpect(status().isCreated());
        assertThat(response.getTitle()).isEqualTo(postRecruitmentDto.getTitle());
        assertThat(response.getContent()).isEqualTo(postRecruitmentDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(postRecruitmentDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(TimeUtils.stringToLocalDate(postRecruitmentDto.getTravelStartDate()));
        assertThat(response.getTravelEndDate()).isEqualTo(TimeUtils.stringToLocalDate(postRecruitmentDto.getTravelEndDate()));
    }

    @Test
    @DisplayName("동행 모집글 수정")
    @WithMockCustomUser
    void buddyRecruitmentControllerTest2() throws Exception {
        // given
        BuddyDto.PatchRecruitment patchRecruitmentDto = StubData.MockBuddy.getPatchRecruitment();
        BuddyDto.PatchResponseRecruitment responseRecruitmentDto = StubData.MockBuddy.getPatchResponseRecruitment();

        given(buddyRecruitmentService.updateBuddyRecruitment(any(),any(),any())).willReturn(responseRecruitmentDto);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1)
                .build().toUri().toString();

        String json = ObjectMapperUtils.dtoToJsonString(patchRecruitmentDto);
        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        BuddyDto.PostResponseRecruitment response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                BuddyDto.PostResponseRecruitment.class);

        actions
                .andExpect(status().isOk());
        assertThat(response.getTitle()).isEqualTo(patchRecruitmentDto.getTitle());
        assertThat(response.getContent()).isEqualTo(patchRecruitmentDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(patchRecruitmentDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(TimeUtils.stringToLocalDate(patchRecruitmentDto.getTravelStartDate()));
        assertThat(response.getTravelEndDate()).isEqualTo(TimeUtils.stringToLocalDate(patchRecruitmentDto.getTravelEndDate()));
    }

    @Test
    @DisplayName("동행 모집글 삭제")
    @WithMockCustomUser
    void buddyRecruitmentControllerTest3() throws Exception {
        // given
        doNothing().when(buddyRecruitmentService).deleteBuddyRecruitment(any(), any());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1 + "/" + "deleted")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithUserDetails(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isNoContent());
    }

}
