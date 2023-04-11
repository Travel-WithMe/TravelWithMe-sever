package com.frog.travelwithme.unit.security.auth.controller;


import com.frog.travelwithme.global.security.auth.controller.AuthController;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.service.AuthService;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import com.frog.travelwithme.utils.snippet.reqeust.ResultActionsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.1
 * 작성일자: 2023/04/10
 **/
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerTest {

    private final String BASE_URL = "/auth";
    @Autowired
    protected MockMvc mvc;
    @MockBean
    protected AuthService authService;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Access token reissue success")
    @WithMockCustomUser
    void accessTokenReissrueTest() throws Exception {
        // given
        given(jwtTokenProvider.resolveAccessToken(any(HttpServletRequest.class))).willReturn("access token");
        given(authService.reissueAccessToken(any())).willReturn("access token");
        doNothing().when(jwtTokenProvider).accessTokenSetHeader(any(), any());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/reissue")
                .build().toUri().toString(); // /auth/reissue
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("access-token-reissue"));
    }

    @Test
    @DisplayName("logout controller test")
    @WithMockCustomUser
    void logoutTest() throws Exception {
        // given
        doNothing().when(authService).logout(any(), any());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/logout")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri);

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("logout"));
    }
}
