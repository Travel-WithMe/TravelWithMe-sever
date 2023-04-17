package com.frog.travelwithme.intergration.buddyrecruitment;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.service.BuddyRecruitmentService;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.intergration.BaseIntegrationTest;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.snippet.reqeust.RequestSnippet;
import com.frog.travelwithme.utils.snippet.response.ResponseSnippet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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
class BuddyRecruitmentIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/recruitments";
    private final String EMAIL = "email@gmail.com";

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    private BuddyRecruitmentService buddyRecruitmentService;

    @BeforeEach
    void befroeEach() {
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        memberService.signUp(signUpDto);
    }

    @AfterEach
    void afterEach() {
        memberService.deleteMember(EMAIL);
    }

    @Test
    @DisplayName("동행 작성 테스트")
    void BuddyRecruitmentIntegrationTest1() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        BuddyDto.PostRecruitment postRecruitmentDto = StubData.MockBuddy.getPostRecruitment();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        String json = ObjectMapperUtils.dtoToJsonString(postRecruitmentDto);

        ResultActions actions = ResultActionsUtils.postRequestWithContentAndToken(
                mvc, uri, json, accessToken, encryptedRefreshToken
        );

        // then
        BuddyDto.ResponseRecruitment response = ObjectMapperUtils.actionsSingleToDto(actions,
                BuddyDto.ResponseRecruitment.class);
        assertThat(response.getTitle()).isEqualTo(postRecruitmentDto.getTitle());
        assertThat(response.getContent()).isEqualTo(postRecruitmentDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(postRecruitmentDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(postRecruitmentDto.getTravelStartDate());
        assertThat(response.getTravelEndDate()).isEqualTo(postRecruitmentDto.getTravelEndDate());
        assertThat(response.getViewCount()).isEqualTo(0L);
        assertThat(response.getCommentCount()).isEqualTo(0L);
        assertThat(response.getNickname()).isEqualTo("nickname");
        assertThat(response.getMemberImage()).isEqualTo("image");


        actions
                .andExpect(status().isCreated())
                .andDo(document("post-recruitment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getRecruitmentSnippet(),
                        ResponseSnippet.getRecruitmentSnippet()));
    }
}
