package com.frog.travelwithme.unit.security.auth.controller.patch;


import com.frog.travelwithme.unit.security.auth.controller.AbstractAuthController;
import com.frog.travelwithme.utils.reqeust.ResultActionsUtils;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/06
 **/
class AuthControllerPatchTest extends AbstractAuthController {
    @Test
    @DisplayName("Access token reissue success")
    @WithMockCustomUser
    void accessTokenReissrueTest() throws Exception {
        // given
        given(jwtTokenProvider.resolveAccessToken(any(HttpServletRequest.class))).willReturn("access token");
        given(authService.reissueAccessToken(any())).willReturn("access token");
        doNothing().when(jwtTokenProvider).accessTokenSetHeader(any(), any());

        // when
        String uri = getResourceUrl("reissue"); // /auth/reissue
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
        String uri = getResourceUrl("logout"); // /auth/reissue
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri);

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("logout"));
    }
}
