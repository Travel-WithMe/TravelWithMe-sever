package com.frog.travelwithme.unit.security;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.security.auth.dto.LoginDto;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.snippet.reqeust.ResultActionsUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/07
 **/
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {
    private final String BASE_URL = "/auth";
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected MemberService memberService;

    @BeforeEach
    void befroeEach() {
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        memberService.signUp(signUpDto);
    }

    @AfterEach
    void afterEach() {
        memberService.deleteMember("email@gmail.com");
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccessTest() throws Exception {
        LoginDto loginDto = StubData.MockMember.getLoginSuccessDto();
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(loginDto);
        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri, json);

        // when // then
        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFailTest() throws Exception {
        // given
        LoginDto loginDto = StubData.MockMember.getLoginFailDto();
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(loginDto);
        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri, json);

        // when // then
        actions
                .andExpect(status().isUnauthorized());
    }
}
