package com.frog.travelwithme.intergration.buddy;

import com.frog.travelwithme.domain.buddy.entity.Buddy;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.BuddyRepository;
import com.frog.travelwithme.domain.recruitment.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.dto.MessageResponseDto;
import com.frog.travelwithme.global.exception.ErrorResponse;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.intergration.BaseIntegrationTest;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.snippet.response.ErrorResponseSnippet;
import com.frog.travelwithme.utils.snippet.response.ResponseSnippet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import static com.frog.travelwithme.global.enums.EnumCollection.*;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class BuddyIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/recruitments";
    private final String SUB_URL = "/buddy";
    private String EMAIL;
    private String EMAIL_OTHER;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private BuddyRepository buddyMatchingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;


    @BeforeEach
    void beforeEach() {
        // e_ma-il@gmail.com 회원 추가
        MemberDto.SignUp memberOne = StubData.MockMember.getSignUpDto();
        memberService.signUp(memberOne);
        EMAIL = memberOne.getEmail();

        // dhfif718@gmail.com 회원 추가
        MemberDto.SignUp memberTwo = StubData.MockMember.getSignUpDtoByEmailAndNickname(
                "dhfif718@gmail.com",
                "이재혁"
        );
        memberService.signUp(memberTwo);
        EMAIL_OTHER = memberTwo.getEmail();
    }

    @Test
    @DisplayName("동행 매칭신청 (신규)")
    void buddyMatchingIntegrationTest1() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL_OTHER).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveBuddy = recruitmentRepository.save(recruitment);
        Long recruitmentId = saveBuddy.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(ResponseBody.NEW_REQUEST_BUDDY.getDescription());
        actions
                .andExpect(status().isOk())
                .andDo(document("post-buddy-request-new",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getBuddyMatchingSnippet()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 (재신청)")
    void buddyMatchingIntegrationTest2() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member user = memberRepository.findByEmail(EMAIL).get();
        Member writer = memberRepository.findByEmail(EMAIL_OTHER).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveBuddy = recruitmentRepository.save(recruitment);
        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.changeStatus(BuddyStatus.REJECT);
        buddy.addMember(user);
        buddy.addRecruitment(saveBuddy);
        saveBuddy.addBuddy(buddy);
        Long recruitmentId = saveBuddy.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(ResponseBody.RETRY_REQUEST_BUDDY.getDescription());
        actions
                .andExpect(status().isOk())
                .andDo(document("post-buddy-request-retry",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getBuddyMatchingSnippet()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 (신청불가)")
    void buddyMatchingIntegrationTest3() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member user = memberRepository.findByEmail(EMAIL).get();
        Member writer = memberRepository.findByEmail(EMAIL_OTHER).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveBuddy = recruitmentRepository.save(recruitment);
        Buddy buddy = StubData.MockBuddy.getBuddy();
        buddy.changeStatus(BuddyStatus.APPROVE);
        buddy.addMember(user);
        buddy.addRecruitment(saveBuddy);
        saveBuddy.addBuddy(buddy);
        Long recruitmentId = saveBuddy.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.BUDDY_REQUEST_NOT_ALLOWED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.BUDDY_REQUEST_NOT_ALLOWED.getMessage());
        actions
                .andDo(document("post-buddy-request-exception",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }
}
