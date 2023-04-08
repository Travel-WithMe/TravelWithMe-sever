package com.frog.travelwithme.unit.security;

import com.frog.travelwithme.global.security.auth.dto.LoginDto;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.reqeust.ResultActionsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityTest extends AbstractSecurityTest {

    @Test
    @DisplayName("로그인 성공")
    void loginSuccessTest() throws Exception {
        LoginDto loginDto = StubData.MockMember.getLoginDto();
        String uri = getResourceUrl("login"); // /auth/login
        String json = ObjectMapperUtils.asJsonString(loginDto);
        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri, json);

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFailTest() throws Exception {
        // given

        // when

        // then

    }

}
