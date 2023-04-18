package com.frog.travelwithme.unit.domain.member.controller;

import com.frog.travelwithme.domain.member.controller.MemberController;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.service.MemberService;
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
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/10
 **/
@WebMvcTest(
        controllers = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class MemberControllerTest {
    private final String BASE_URL = "/members";
    @Autowired
    protected MockMvc mvc;
    @MockBean
    protected MemberService memberService;
    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("회원가입")
    @WithMockCustomUser
    void memberControllerTest1() throws Exception {
        // given
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.signUp(any(MemberDto.SignUp.class))).willReturn(response);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/signup")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(signUpDto);
        ResultActions actions = ResultActionsUtils.postRequestWithContent(mvc, uri, json);

        // then
        actions
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원 정보 수정")
    @WithMockCustomUser(email = "email", password = "password")
    void memberControllerTest2() throws Exception {
        // given
        MemberDto.Patch patchDto = StubData.MockMember.getPatchDto();
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.updateMember(any(MemberDto.Patch.class), any())).willReturn(response);

        // when
        String json = ObjectMapperUtils.asJsonString(patchDto);
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndUserDetails(mvc, uri, json, userDetails);

        // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 조회")
    @WithMockCustomUser
    void memberControllerTest3() throws Exception {
        // given
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.findMemberByEmail(any())).willReturn(response);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithUserDetails(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 삭제")
    @WithMockCustomUser
    void memberControllerTest4() throws Exception {
        // given
        doNothing().when(memberService).deleteMember(any());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.deleteRequestWithUserDetails(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isNoContent());
    }
}
