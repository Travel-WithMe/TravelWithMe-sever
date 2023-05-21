package com.frog.travelwithme.unit.domain.buddy.controller;

import com.frog.travelwithme.domain.buddy.controller.RecruitmentController;
import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.buddy.service.RecruitmentService;
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

import java.util.ArrayList;
import java.util.List;

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
                RecruitmentController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class RecruitmentControllerTest {

    private final String BASE_URL = "/recruitments";

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected RecruitmentService recruitmentService;

    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("동행 모집글 작성")
    @WithMockCustomUser
    void recruitmentControllerTest1() throws Exception {
        // given
        RecruitmentDto.Post postDto = StubData.MockRecruitment.getPostRecruitment();
        RecruitmentDto.PostResponse responseRecruitmentDto = StubData.MockRecruitment.getPostResponseRecruitment();

        given(recruitmentService.createRecruitmentByEmail(any(),any())).willReturn(responseRecruitmentDto);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();

        String json = ObjectMapperUtils.objectToJsonString(postDto);
        ResultActions actions = ResultActionsUtils.postRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        RecruitmentDto.PostResponse response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                RecruitmentDto.PostResponse.class);

        actions
                .andExpect(status().isCreated());
        assertThat(response.getTitle()).isEqualTo(postDto.getTitle());
        assertThat(response.getContent()).isEqualTo(postDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(postDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(TimeUtils.stringToLocalDate(postDto.getTravelStartDate()));
        assertThat(response.getTravelEndDate()).isEqualTo(TimeUtils.stringToLocalDate(postDto.getTravelEndDate()));
    }

    @Test
    @DisplayName("동행 모집글 수정")
    @WithMockCustomUser
    void recruitmentControllerTest2() throws Exception {
        // given
        RecruitmentDto.Patch patchDto = StubData.MockRecruitment.getPatchRecruitment();
        RecruitmentDto.PatchResponse responseRecruitmentDto = StubData.MockRecruitment.getPatchResponseRecruitment();

        given(recruitmentService.updateRecruitmentByEmail(any(),any(),any())).willReturn(responseRecruitmentDto);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1)
                .build().toUri().toString();

        String json = ObjectMapperUtils.objectToJsonString(patchDto);
        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        RecruitmentDto.PostResponse response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                RecruitmentDto.PostResponse.class);

        actions
                .andExpect(status().isOk());
        assertThat(response.getTitle()).isEqualTo(patchDto.getTitle());
        assertThat(response.getContent()).isEqualTo(patchDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(patchDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(TimeUtils.stringToLocalDate(patchDto.getTravelStartDate()));
        assertThat(response.getTravelEndDate()).isEqualTo(TimeUtils.stringToLocalDate(patchDto.getTravelEndDate()));
    }

    @Test
    @DisplayName("동행 모집글 삭제")
    @WithMockCustomUser
    void recruitmentControllerTest3() throws Exception {
        // given
        doNothing().when(recruitmentService).deleteRecruitmentByEmail(any(), any());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + 1)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.deleteRequestWithUserDetails(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("동행 모집글 매칭신청 회원 리스트 조회")
    @WithMockCustomUser
    void recruitmentControllerTest4() throws Exception {
        // given
        RecruitmentDto.MatchingRequestMemberResponse matchingRequestMemberResponse1
                = StubData.MockMember.getMatchingRequestMemberResponse(1L, "dhfif718");
        RecruitmentDto.MatchingRequestMemberResponse matchingRequestMemberResponse2
                = StubData.MockMember.getMatchingRequestMemberResponse(2L, "kkd718");
        RecruitmentDto.MatchingRequestMemberResponse matchingRequestMemberResponse3
                = StubData.MockMember.getMatchingRequestMemberResponse(3L, "리젤란");

        List<RecruitmentDto.MatchingRequestMemberResponse> matchingRequestMemberResponseList = new ArrayList<>();
        matchingRequestMemberResponseList.add(matchingRequestMemberResponse1);
        matchingRequestMemberResponseList.add(matchingRequestMemberResponse2);
        matchingRequestMemberResponseList.add(matchingRequestMemberResponse3);

        given(recruitmentService.getMatchingRequestMemberList(any())).willReturn(matchingRequestMemberResponseList);

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + 1 + "/" + "matching-request-list")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri);

        // then
        List<RecruitmentDto.MatchingRequestMemberResponse> response = List.of(ObjectMapperUtils.actionsSingleToResponseWithData(
                actions, RecruitmentDto.MatchingRequestMemberResponse[].class
        ));


        actions
                .andExpect(status().isOk());
        for (int i = 0; i < response.size(); i++) {
            assertThat(response.get(i).getId()).isEqualTo(matchingRequestMemberResponseList.get(i).getId());
            assertThat(response.get(i).getNickname()).isEqualTo(matchingRequestMemberResponseList.get(i).getNickname());
            assertThat(response.get(i).getImage()).isEqualTo(matchingRequestMemberResponseList.get(i).getImage());
        }
    }
}
