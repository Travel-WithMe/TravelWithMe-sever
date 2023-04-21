package com.frog.travelwithme.intergration.member;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.Response;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.intergration.BaseIntegrationTest;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.snippet.reqeust.RequestSnippet;
import com.frog.travelwithme.utils.snippet.response.ResponseSnippet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class MemberIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/members";
    private final String EMAIL = StubData.MockMember.getEmail();
    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @BeforeEach
    void beforeEach() {
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        memberService.signUp(signUpDto);
    }

    @Test
    @DisplayName("회원가입")
    void memberIntegrationTest1() throws Exception {
        // given
        memberService.deleteMember(EMAIL);
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/signup")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(signUpDto);
        ResultActions actions = ResultActionsUtils.postRequestWithContent(mvc, uri, json);

        // then
        Response response = ObjectMapperUtils.actionsSingleToDto(actions, Response.class);
        assertThat(signUpDto.getEmail()).isEqualTo(response.getEmail());
        assertThat(signUpDto.getNickname()).isEqualTo(response.getNickname());
        assertThat(signUpDto.getAddress()).isEqualTo(response.getAddress());
        assertThat(signUpDto.getIntroduction()).isEqualTo(response.getIntroduction());
        assertThat(signUpDto.getNation()).isEqualTo(response.getNation());
        assertThat(signUpDto.getRole()).isEqualTo(response.getRole());
        actions
                .andExpect(status().isCreated())
                .andDo(document("signup",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getSignUpSnippet(),
                        ResponseSnippet.getMemberSnippet()));
    }

    @Test
    @DisplayName("회원 정보 수정")
    void memberIntegrationTest2() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        Response originMemberDto = memberService.findMemberByEmail(EMAIL);
        MemberDto.Patch patchDto = StubData.MockMember.getPatchDto();

        // when
        String json = ObjectMapperUtils.asJsonString(patchDto);
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.
                patchRequestWithContentAndToken(mvc, uri, json, accessToken, encryptedRefreshToken);

        // then
        Response response = ObjectMapperUtils.actionsSingleToDto(actions, Response.class);
        assertThat(originMemberDto.getNickname()).isNotEqualTo(response.getNickname());
        assertThat(originMemberDto.getAddress()).isNotEqualTo(response.getAddress());
        assertThat(originMemberDto.getIntroduction()).isNotEqualTo(response.getIntroduction());
        assertThat(originMemberDto.getNation()).isNotEqualTo(response.getNation());
        assertThat(patchDto.getNickname()).isEqualTo(response.getNickname());
        assertThat(patchDto.getAddress()).isEqualTo(response.getAddress());
        assertThat(patchDto.getIntroduction()).isEqualTo(response.getIntroduction());
        assertThat(patchDto.getNation()).isEqualTo(response.getNation());
        actions
                .andExpect(status().isOk())
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getMemberPatchSnippet(),
                        ResponseSnippet.getMemberSnippet()));
    }

    @Test
    @DisplayName("회원 조회")
    void memberIntegrationTest3() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithToken(mvc, uri, accessToken, encryptedRefreshToken);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-member",
                        getResponsePreProcessor(),
                        ResponseSnippet.getMemberSnippet()));
    }

    @Test
    @DisplayName("회원 삭제")
    void memberIntegrationTest4() throws Exception {
        // given
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.deleteRequestWithToken(mvc, uri, accessToken, encryptedRefreshToken);

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-member"));
        memberService.signUp(signUpDto);
    }
}
