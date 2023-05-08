package com.frog.travelwithme.unit.domain.feed.controller;

import com.frog.travelwithme.domain.feed.controller.FeedController;
import com.frog.travelwithme.domain.feed.controller.dto.FeedDto;
import com.frog.travelwithme.domain.feed.controller.dto.TagDto;
import com.frog.travelwithme.domain.feed.service.FeedService;
import com.frog.travelwithme.domain.feed.service.TagService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/17
 **/
@WebMvcTest(
        controllers = FeedController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class FeedControllerTest {

    private final String BASE_URL = "/feed";

    private final String TAG_NAME = StubData.MockFeed.getTagName();

    private final int SIZE = StubData.MockFeed.getSize();
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FeedService feedService;

    @MockBean
    private TagService tagService;

    @Mock
    private CustomUserDetails userDetails;

    @Test
    @DisplayName("피드 작성")
    @WithMockCustomUser
    void feedControllerTest1() throws Exception {
        // given
        FeedDto.Post postDto = StubData.MockFeed.getPostDto();
        FeedDto.Response response = StubData.MockFeed.getResponseDto();
        given(feedService.postFeed(any(), any(FeedDto.Post.class))).willReturn(response);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(postDto);
        ResultActions actions = ResultActionsUtils.postRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        actions
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("피드 조회")
    @WithMockCustomUser
    void feedControllerTest2() throws Exception {
        // given
        FeedDto.Response response = StubData.MockFeed.getResponseDto();
        given(feedService.findFeedById(any(), anyLong())).willReturn(response);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/1")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithUserDetails(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("모든 피드 조회")
    @WithMockCustomUser
    void feedControllerTest3() throws Exception {
        // given
        List<FeedDto.Response> responseDtos = StubData.MockFeed.getResponseDtos();
        given(feedService.findAll(anyLong(), any())).willReturn(responseDtos);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithUserDetails(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드 수정")
    @WithMockCustomUser
    void feedControllerTest4() throws Exception {
        // given
        FeedDto.Patch patchDto = StubData.MockFeed.getPatchDto();
        FeedDto.Response response = StubData.MockFeed.getResponseDto();
        given(feedService.updateFeed(any(), anyLong(), any(FeedDto.Patch.class))).willReturn(response);

        // when
        String json = ObjectMapperUtils.asJsonString(patchDto);
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/1")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드 삭제")
    @WithMockCustomUser
    void feedControllerTest5() throws Exception {
        // given
        doNothing().when(feedService).deleteFeed(any(), anyLong());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/1")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.deleteRequestWithUserDetails(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Tag Name과 유사한 모든 TAG 조회")
    @WithMockCustomUser
    void feedControllerTest6() throws Exception {
        // given
        List<TagDto.Response> expectedResponse = StubData.MockFeed.getTagResponseDtoList(2);
        given(tagService.findTagsStartWith(any(), anyInt())).willReturn(expectedResponse);
        String tagName = StubData.MockFeed.getTagName();
        MultiValueMap<String, String> tagNamePapram = new LinkedMultiValueMap<>();
        tagNamePapram.add(tagName, tagName);
        MultiValueMap<String, String> sizeParam = new LinkedMultiValueMap<>();
        tagNamePapram.add(TAG_NAME, TAG_NAME);
        sizeParam.add("size", String.valueOf(SIZE));

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/tags")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithTwoParams(mvc, uri, tagNamePapram, sizeParam);

        // then
        actions
                .andExpect(status().isOk());
    }
}
