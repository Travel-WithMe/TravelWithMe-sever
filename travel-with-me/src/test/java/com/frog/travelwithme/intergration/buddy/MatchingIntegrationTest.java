package com.frog.travelwithme.intergration.buddy;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.MatchingRepository;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
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

import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.*;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class MatchingIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/recruitments";
    private final String SUB_URL = "/matching";
    private String EMAIL;
    private String EMAIL_OTHER;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private MatchingRepository matchingRepository;

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
    void matchingIntegrationTest1() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL_OTHER).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = saveRecruitment.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                        "/" + "request").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        Optional<Recruitment> findRecruitment = recruitmentRepository.findById(recruitmentId);

        assertThat(response).isEqualTo(ResponseBody.NEW_REQUEST_MATCHING.getDescription());
        assertThat(findRecruitment.get().getMatchingList().get(0).getStatus()).isEqualTo(MatchingStatus.REQUEST);
        actions
                .andExpect(status().isOk())
                .andDo(document("post-matching-request-new",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getMatchingSnippet()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 (재신청)")
    void matchingIntegrationTest2() throws Exception {
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
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.reject();
        matching.addMember(user);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Long recruitmentId = saveRecruitment.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + "request").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        Optional<Recruitment> findRecruitment = recruitmentRepository.findById(recruitmentId);

        assertThat(response).isEqualTo(ResponseBody.RETRY_REQUEST_MATCHING.getDescription());
        assertThat(findRecruitment.get().getMatchingList().get(0).getStatus()).isEqualTo(MatchingStatus.REQUEST);
        actions
                .andExpect(status().isOk())
                .andDo(document("post-matching-request-retry",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getMatchingSnippet()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 (모집이 종료된 게시글)")
    void matchingIntegrationTest3() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL_OTHER).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        recruitment.end();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = saveRecruitment.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + "request").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getMessage());
        actions
                .andDo(document("post-matching-request-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 (신청불가)")
    void matchingIntegrationTest4() throws Exception {
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
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.approve();
        matching.addMember(user);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Long recruitmentId = saveRecruitment.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + "request").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.MATCHING_REQUEST_NOT_ALLOWED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.MATCHING_REQUEST_NOT_ALLOWED.getMessage());
        actions
                .andDo(document("post-matching-request-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭취소")
    void matchingIntegrationTest5() throws Exception {
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
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Long recruitmentId = saveRecruitment.getId();


        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + "cancel").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(ResponseBody.CANCEL_MATCHING.getDescription());

        Optional<Matching> findMatching = matchingRepository.findMatchingByMemberAndRecruitment(user, saveRecruitment);
        assertThat(findMatching.get().getStatus()).isEqualTo(MatchingStatus.CANCEL);

        actions
                .andExpect(status().isOk())
                .andDo(document("post-matching-cancel",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getMatchingSnippet()
                ));
    }

    @Test
    @DisplayName("동행 매칭취소 (모집이 종료된 게시글)")
    void matchingIntegrationTest6() throws Exception {
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
        recruitment.end();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Long recruitmentId = saveRecruitment.getId();


        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + "cancel").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getMessage());

        actions
                .andDo(document("post-matching-cancel-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭취소 (매칭 이력이 없음)")
    void matchingIntegrationTest7() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member user = memberRepository.findByEmail(EMAIL_OTHER).get();
        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(user);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Long recruitmentId = saveRecruitment.getId();


        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + "cancel").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.MATCHING_NOT_FOUND.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.MATCHING_NOT_FOUND.getMessage());
        actions
                .andDo(document("post-matching-cancel-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭취소 (취소불가)")
    void matchingIntegrationTest8() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.reject();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Long recruitmentId = saveRecruitment.getId();


        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + "cancel").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.MATCHING_CANCEL_NOT_ALLOWED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.MATCHING_CANCEL_NOT_ALLOWED.getMessage());
        actions
                .andDo(document("post-matching-cancel-exception-3",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 승인")
    void matchingIntegrationTest9() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "approve").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        assertThat(response).isEqualTo(ResponseBody.APPROVE_MATCHING.getDescription());

        Optional<Matching> findMatching = matchingRepository.findMatchingById(matchingId);
        assertThat(findMatching.get().getStatus()).isEqualTo(MatchingStatus.APPROVE);
        actions
                .andDo(document("post-matching-approve",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getMatchingSnippet()
                ));
    }


    @Test
    @DisplayName("동행 매칭신청 승인 (모집이 종료된 게시글)")
    void matchingIntegrationTest10() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        recruitment.end();

        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "approve").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getMessage());
        actions
                .andDo(document("post-matching-approve-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }


    @Test
    @DisplayName("동행 매칭신청 승인 (동행글 작성자가 아님)")
    void matchingIntegrationTest11() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Member user = memberRepository.findByEmail(EMAIL_OTHER).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(user);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "approve").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_WRITER_NOT_MATCH.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_WRITER_NOT_MATCH.getMessage());

        actions
                .andDo(document("post-matching-approve-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 승인 (동행 모집글이 다름)")
    void matchingIntegrationTest12() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment recruitmentOther = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        recruitmentOther.addMember(writer);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Recruitment saveRecruitmentOther = recruitmentRepository.save(recruitmentOther);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitmentOther);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "approve").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.MATCHING_RECRUITMENT_IS_DIFFERENT.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.MATCHING_RECRUITMENT_IS_DIFFERENT.getMessage());
        actions
                .andDo(document("post-matching-approve-exception-3",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 거절")
    void matchingIntegrationTest13() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "reject").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        String response = ObjectMapperUtils.actionsSingleToString(actions, MessageResponseDto.class);
        Optional<Matching> findMatching = matchingRepository.findMatchingById(matchingId);

        assertThat(response).isEqualTo(ResponseBody.REJECT_MATCHING.getDescription());
        assertThat(findMatching.get().getStatus()).isEqualTo(MatchingStatus.REJECT);
        actions
                .andDo(document("post-matching-reject",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getMatchingSnippet()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 거절 (모집이 종료된 게시글)")
    void matchingIntegrationTest14() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        recruitment.end();

        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "reject").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getMessage());
        actions
                .andDo(document("post-matching-reject-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 거절 (동행글 작성자가 아님)")
    void matchingIntegrationTest15() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Member user = memberRepository.findByEmail(EMAIL_OTHER).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(user);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitment);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "reject").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_WRITER_NOT_MATCH.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_WRITER_NOT_MATCH.getMessage());

        actions
                .andDo(document("post-matching-reject-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 매칭신청 거절 (동행 모집글이 다름)")
    void matchingIntegrationTest16() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberRepository.findByEmail(EMAIL).get();
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment recruitmentOther = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        recruitmentOther.addMember(writer);
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        Recruitment saveRecruitmentOther = recruitmentRepository.save(recruitmentOther);
        Matching matching = StubData.MockMatching.getMatching();
        matching.addMember(writer);
        matching.addRecruitment(saveRecruitmentOther);
        saveRecruitment.addMatching(matching);
        Matching savedMatching = matchingRepository.save(matching);
        Long recruitmentId = saveRecruitment.getId();
        Long matchingId = savedMatching.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId + "/" + SUB_URL +
                "/" + matchingId + "/" + "reject").build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.MATCHING_RECRUITMENT_IS_DIFFERENT.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.MATCHING_RECRUITMENT_IS_DIFFERENT.getMessage());
        actions
                .andDo(document("post-matching-reject-exception-3",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

}
