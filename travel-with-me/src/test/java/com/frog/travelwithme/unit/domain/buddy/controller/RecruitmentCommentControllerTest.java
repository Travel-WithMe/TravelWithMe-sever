package com.frog.travelwithme.unit.domain.buddy.controller;

import com.frog.travelwithme.domain.buddy.controller.RecruitmentCommentController;
import com.frog.travelwithme.domain.buddy.service.RecruitmentCommentService;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/31
 **/

@WebMvcTest(
        controllers = {
                RecruitmentCommentController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class RecruitmentCommentControllerTest {

    private final String BASE_URL = "/recruitments";
    private final String SUB_URL = "/comments";

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected RecruitmentCommentService recruitmentCommentService;

    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 작성")
    @WithMockCustomUser
    void recruitmentCommentControllerTest1() throws Exception {
        // given

        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(1, null,1L);
        CommentDto.PostResponse postResponseDto = StubData.MockComment.getPostResponseDto();

        given(recruitmentCommentService.createCommentByEmail(any(),any(),any())).willReturn(postResponseDto);

        // when
        String uri = BASE_URL + "/1" + SUB_URL;

        String json = ObjectMapperUtils.objectToJsonString(postDto);
        ResultActions actions = ResultActionsUtils.postRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        CommentDto.PostResponse response =
                ObjectMapperUtils.actionsSingleToResponseWithData(actions, CommentDto.PostResponse.class);

        actions
                .andExpect(status().isCreated());
        assertThat(response.getCommentId()).isEqualTo(postResponseDto.getCommentId());
        assertThat(response.getDepth()).isEqualTo(postResponseDto.getDepth());
        assertThat(response.getTaggedMemberId()).isEqualTo(postResponseDto.getTaggedMemberId());
        assertThat(response.getContent()).isEqualTo(postResponseDto.getContent());
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 수정")
    @WithMockCustomUser
    void recruitmentCommentControllerTest2() throws Exception {
        // given

        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경확인", 1L);
        CommentDto.PatchResponse patchResponseDto = StubData.MockComment.getPatchResponseDto();

        given(recruitmentCommentService.updateCommentByEmail(any(),any(),any())).willReturn(patchResponseDto);

        // when
        String uri = BASE_URL + SUB_URL + "/1";

        String json = ObjectMapperUtils.objectToJsonString(patchDto);
        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        CommentDto.PatchResponse response =
                ObjectMapperUtils.actionsSingleToResponseWithData(actions, CommentDto.PatchResponse.class);

        actions
                .andExpect(status().isOk());
        assertThat(response.getCommentId()).isEqualTo(patchResponseDto.getCommentId());
        assertThat(response.getDepth()).isEqualTo(patchResponseDto.getDepth());
        assertThat(response.getTaggedMemberId()).isEqualTo(patchResponseDto.getTaggedMemberId());
        assertThat(response.getContent()).isEqualTo(patchResponseDto.getContent());
    }
}
