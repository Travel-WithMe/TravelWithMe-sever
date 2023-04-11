package com.frog.travelwithme.unit.domain.member.controller;

import com.frog.travelwithme.domain.member.controller.MemberController;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import com.frog.travelwithme.utils.snippet.reqeust.MemberRequestSnippet;
import com.frog.travelwithme.utils.snippet.reqeust.ResultActionsUtils;
import com.frog.travelwithme.utils.snippet.response.MemberResponseSnippet;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerTest {
    private final String BASE_URL = "/members";
    @Autowired
    protected MockMvc mvc;
    @MockBean
    protected MemberService memberService;
    @Mock
    protected CustomUserDetails userDetails;

    @Test
    @DisplayName("Sign up test")
    @WithMockCustomUser
    void signUpTest() throws Exception {
        // given
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.signUp(any(MemberDto.SignUp.class))).willReturn(response);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/signup")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(signUpDto);
        ResultActions actions = ResultActionsUtils.postRequest(mvc, uri, json);

        // then
        actions
                .andExpect(status().isCreated())
                .andDo(document("signup",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        MemberRequestSnippet.getSignUpSnippet(),
                        MemberResponseSnippet.getMemberResponseSnippet()));
    }

    @Test
    @DisplayName("Patch member test")
    @WithMockCustomUser(email = "email", password = "password")
    void patchMemberTest() throws Exception {
        // given
        MemberDto.Patch patchDto = StubData.MockMember.getPatchDto();
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.updateMember(any(MemberDto.Patch.class), any())).willReturn(response);

        // when
        String json = ObjectMapperUtils.asJsonString(patchDto);
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri, json, userDetails);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        MemberRequestSnippet.getPatchSnippet(),
                        MemberResponseSnippet.getMemberResponseSnippet()));
    }

    @Test
    @DisplayName("Get member test")
    @WithMockCustomUser
    void getMemberTest() throws Exception {
        // given
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.findMemberByEmail(any())).willReturn(response);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-member",
                        getResponsePreProcessor(),
                        MemberResponseSnippet.getMemberResponseSnippet()));
    }

    @Test
    @DisplayName("Delete member test")
    @WithMockCustomUser
    void deleteMemberTest() throws Exception {
        // given
        doNothing().when(memberService).deleteMember(any());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.deleteRequest(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-member",
                        getResponsePreProcessor()));
    }
}
