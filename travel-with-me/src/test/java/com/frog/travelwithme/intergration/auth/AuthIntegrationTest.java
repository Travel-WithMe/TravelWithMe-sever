package com.frog.travelwithme.intergration.auth;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.redis.RedisService;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginDto;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginResponse;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.intergration.BaseIntegrationTest;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.snippet.reqeust.ResultActionsUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.frog.travelwithme.utils.snippet.reqeust.AuthRequestSnippet.getLoginSnippet;
import static com.frog.travelwithme.utils.snippet.response.AuthResponseSnippet.getLonginSuccessResponseSnippet;
import static com.frog.travelwithme.utils.snippet.response.ErrorResponseSnippet.getFieldErrorSnippets;
import static com.frog.travelwithme.utils.snippet.response.ErrorResponseSnippet.getFieldErrorSnippetsLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/10
 **/
class AuthIntegrationTest extends BaseIntegrationTest {
    private final String BASE_URL = "/auth";
    private final String EMAIL = "email@gmail.com";
    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

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
        // given
        LoginDto loginSuccessDto = StubData.MockMember.getLoginSuccessDto();
        LoginResponse expectedResponseDto = StubData.MockMember.getLoginResponseDto();
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(loginSuccessDto);
        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri, json);
        LoginResponse responseDto = ObjectMapperUtils.actionsSingleResponseToLoginDto(actions);

        // when // then
        assertThat(expectedResponseDto.getEmail()).isEqualTo(responseDto.getEmail());
        assertThat(expectedResponseDto.getNickname()).isEqualTo(responseDto.getNickname());
        assertThat(expectedResponseDto.getRole()).isEqualTo(responseDto.getRole());
        actions
                .andExpect(status().isOk())
                .andDo(document("login-success",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        getLoginSnippet(),
                        getLonginSuccessResponseSnippet()));
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFailTest() throws Exception {
        // given
        LoginDto loginFailDto = StubData.MockMember.getLoginFailDto();

        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/login")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(loginFailDto);
        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri, json);

        // when // then
        actions
                .andExpect(status().isUnauthorized())
                .andDo(document("login-fail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        getLoginSnippet(),
                        getFieldErrorSnippets()));
    }

    @Test
    @DisplayName("Access token 재발급 성공")
    void accessTokenReissrueSuccessTest() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String refreshToken = tokenDto.getRefreshToken();
        redisService.setValues(EMAIL, refreshToken, Duration.ofMillis(10000));
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/reissue")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri, encryptedRefreshToken);

        // when // then
        actions
                .andExpect(status().isOk())
                .andDo(document("access-token-reissue-success"));
    }

    @Test
    @DisplayName("Refresh token 불일치로 Access token 재발급 실패")
    void accessTokenReissrueFailTest() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String refreshToken = tokenDto.getRefreshToken();
        String failRefreshToken = refreshToken + "fail";
        redisService.setValues("email@gmail.com", failRefreshToken, Duration.ofMillis(10000));
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/reissue")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri, encryptedRefreshToken);

        // when // then
        actions
                .andExpect(status().is(404))
                .andDo(document("reissue-fail-by-token-not-same",
                        getResponsePreProcessor(),
                        getFieldErrorSnippetsLong()));
    }

    @Test
    @DisplayName("Header에 Refresh token이 존재하지 않으면 Access token 재발급 실패")
    void accessTokenReissrueFailTest2() throws Exception {
        // given
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/reissue")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri);

        // when // then
        actions
                .andExpect(status().is(404))
                .andDo(document("reissue-fail-by-no-refresh-token-in-header",
                        getResponsePreProcessor(),
                        getFieldErrorSnippetsLong()));
    }

    @Test
    @DisplayName("로그아웃")
    void logoutTest() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        redisService.setValues(EMAIL, refreshToken, Duration.ofMillis(10000));

        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/logout")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri, accessToken, encryptedRefreshToken);

        // when
        String redisRefreshToken = redisService.getValues(EMAIL);
        String logout = redisService.getValues(accessToken);

        // then
        assertThat(redisRefreshToken).isEqualTo("false");
        assertThat(logout).isEqualTo("logout");
        actions
                .andExpect(status().isNoContent())
                .andDo(document("logout"));
    }
}
