package com.frog.travelwithme.intergration.member;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.EmailVerificationResult;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.Response;
import com.frog.travelwithme.domain.member.entity.Follow;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.FollowRepository;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.member.service.InterestService;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.redis.RedisService;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.intergration.BaseIntegrationTest;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.StubData.MockMember;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import com.frog.travelwithme.utils.snippet.reqeust.RequestSnippet;
import com.frog.travelwithme.utils.snippet.response.ResponseSnippet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;

import static com.frog.travelwithme.global.enums.EnumCollection.ResponseBody.SUCCESS_MEMBER_FOLLOW;
import static com.frog.travelwithme.global.enums.EnumCollection.ResponseBody.SUCCESS_MEMBER_UNFOLLOW;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class MemberIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/members";

    private final String EMAIL = MockMember.getEmail();

    private final String EMAIL_KEY = MockMember.getEmailKey();

    private final String EMAIL_VALUE = MockMember.getEmail();

    private final String CODE_KEY = MockMember.getCodeKey();

    private final String CODE_VALUE = MockMember.getCodeValue();

    private final String AUTH_CODE_PREFIX = MockMember.getAuthCodePrefix();

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private InterestService interestService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    @BeforeEach
    void beforeEach() throws MalformedURLException {
        given(amazonS3.putObject(any(PutObjectRequest.class))).willReturn(new PutObjectResult());
        given(amazonS3.getUrl(any(), any())).willReturn(
                new URL(StubData.CustomMultipartFile.getIMAGE_URL()));

        MultipartFile file = StubData.CustomMultipartFile.getMultipartFile();
        MemberDto.SignUp signUpDto = MockMember.getSignUpDto();
        memberService.signUp(signUpDto, file);
    }

    @Test
    @DisplayName("회원가입")
    void memberIntegrationTest1() throws Exception {
        // given
        MockMultipartFile file = StubData.CustomMockMultipartFile.getFile();
        memberService.deleteMember(EMAIL);
        MemberDto.SignUp signUpDto = MockMember.getSignUpDto();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/signup")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(signUpDto);
        MockMultipartFile data = StubData.CustomMockMultipartFile.getData(json);
        ResultActions actions = ResultActionsUtils.postRequestWithTwoMultiPart(mvc, uri, file, data);

        // then
        Response response = ObjectMapperUtils.actionsSingleToResponseWithData(actions, Response.class);
        assertThat(signUpDto.getEmail()).isEqualTo(response.getEmail());
        assertThat(signUpDto.getNickname()).isEqualTo(response.getNickname());
        assertThat(signUpDto.getAddress()).isEqualTo(response.getAddress());
        assertThat(signUpDto.getIntroduction()).isEqualTo(response.getIntroduction());
        assertThat(signUpDto.getNation()).isEqualTo(response.getNation());
        assertThat(signUpDto.getRole()).isEqualTo(response.getRole());
        assertThat(response.getImage()).isNotNull();
        actions
                .andExpect(status().isCreated())
                .andDo(document("signup",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getSignUpMultipartSnippet(),
                        RequestSnippet.getSignUpMultipartDataFieldSnippet(),
                        ResponseSnippet.getMemberSnippet()));
    }

    @Test
    @DisplayName("회원 정보 수정")
    void memberIntegrationTest2() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        Response originMemberDto = memberService.findMemberByEmail(EMAIL);
        MemberDto.Patch patchDto = MockMember.getPatchDto();

        // when
        String json = ObjectMapperUtils.asJsonString(patchDto);
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.
                patchRequestWithContentAndToken(mvc, uri, json, accessToken, encryptedRefreshToken);

        // then
        Response response = ObjectMapperUtils.actionsSingleToResponseWithData(actions, Response.class);
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
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getMemberPatchSnippet(),
                        ResponseSnippet.getMemberSnippet()));
    }

    @Test
    @DisplayName("회원 조회")
    void memberIntegrationTest3() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        // when
        String uri = BASE_URL + "/{email}";
        ResultActions actions = ResultActionsUtils.
                getRequestWithTokenAndPathVariable(mvc, uri, MockMember.getEmail(), accessToken, encryptedRefreshToken);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getEmailPathVariableSnippet(),
                        ResponseSnippet.getMemberSnippet()));
    }

    @Test
    @DisplayName("회원 삭제")
    void memberIntegrationTest4() throws Exception {
        // given
        MemberDto.SignUp signUpDto = MockMember.getSignUpDto();
        CustomUserDetails userDetails = MockMember.getUserDetails();
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
                .andDo(document("delete-member",
                        getRequestPreProcessor(),
                        RequestSnippet.getTokenSnippet()));
    }

    @Test
    @DisplayName("메일을 전송합니다")
    void memberIntegrationTest5() throws Exception {
        // given
        memberService.deleteMember(EMAIL);
        MultiValueMap<String, String> papram = new LinkedMultiValueMap<>();
        papram.add(EMAIL_KEY, EMAIL_VALUE);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/emails/verification-requests")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.postRequestWithParams(mvc, uri, papram);

        // then
        actions.andExpect(status().isOk())
                .andDo(document("email-verification-request",
                        getRequestPreProcessor(),
                        RequestSnippet.getMailVerificiationRequestSnippet()));

        redisService.deleteValues(AUTH_CODE_PREFIX + EMAIL_VALUE);
    }

    @Test
    @DisplayName("인증 번호를 통해 메일을 인증합니다")
    void memberIntegrationTest6() throws Exception {
        // given
        redisService.setValues(AUTH_CODE_PREFIX + EMAIL_VALUE, CODE_VALUE);
        memberService.deleteMember(EMAIL);
        MultiValueMap<String, String> emailPapram = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> codePapram = new LinkedMultiValueMap<>();
        emailPapram.add(EMAIL_KEY, EMAIL_VALUE);
        codePapram.add(CODE_KEY, CODE_VALUE);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/emails/verifications")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithTwoParams(mvc, uri, emailPapram, codePapram);

        // then
        EmailVerificationResult response = ObjectMapperUtils.
                actionsSingleToResponseWithData(actions, EmailVerificationResult.class);
        assertThat(response.isSuccess()).isTrue();
        actions.andExpect(status().isOk())
                .andDo(document("email-verification-success",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getMailVerificiationSnippet(),
                        ResponseSnippet.getMailVerificationSnippet()));

        redisService.deleteValues(AUTH_CODE_PREFIX + EMAIL_VALUE);
    }

    @Test
    @DisplayName("인증 번호가 다르면 인증에 실패합니다")
    void memberIntegrationTest7() throws Exception {
        // given
        redisService.setValues(AUTH_CODE_PREFIX + EMAIL_VALUE, CODE_VALUE);
        memberService.deleteMember(EMAIL);
        MultiValueMap<String, String> emailPapram = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> codePapram = new LinkedMultiValueMap<>();
        emailPapram.add(EMAIL_KEY, EMAIL_VALUE);
        codePapram.add(CODE_KEY, "fail" + CODE_VALUE);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/emails/verifications")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithTwoParams(mvc, uri, emailPapram, codePapram);

        // then
        EmailVerificationResult response = ObjectMapperUtils.
                actionsSingleToResponseWithData(actions, EmailVerificationResult.class);
        assertThat(response.isSuccess()).isFalse();
        actions.andExpect(status().isOk())
                .andDo(document("email-verification-fail",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getMailVerificiationSnippet(),
                        ResponseSnippet.getMailVerificationSnippet()));

        redisService.deleteValues(AUTH_CODE_PREFIX + EMAIL_VALUE);
    }

    @Test
    @DisplayName("회원의 프로필 이미지를 수정합니다.")
    @WithMockCustomUser
    void memberControllerTest8() throws Exception {
        // given
        MockMultipartFile file = StubData.CustomMockMultipartFile.getFile();
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        Response originMemberDto = memberService.findMemberByEmail(EMAIL);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/images")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.patchRequestWithMultiPartAndToken(
                mvc, uri, file, accessToken, encryptedRefreshToken);

        // then
        Response response = ObjectMapperUtils.actionsSingleToResponseWithData(actions, Response.class);
        actions
                .andExpect(status().isOk())
                .andDo(document("patch-profile-image",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getProfileImageMultipartSnippet(),
                        ResponseSnippet.getMemberSnippet()));
    }

    @Test
    @DisplayName("회원가입 시 성별은 남자, 여자만 입력 가능")
    void memberIntegrationTest9() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file",
                "originalFilename", "text/plain", "fileContent".getBytes());
        memberService.deleteMember(EMAIL);
        MockMember.MockGenderFailSingUp failedSignUpDto =
                MockMember.getFailedSignUpDtoByGender("중성");

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/signup")
                .build().toUri().toString();
        String json = ObjectMapperUtils.asJsonString(failedSignUpDto);
        MockMultipartFile data =
                new MockMultipartFile("data", null, MediaType.APPLICATION_JSON_VALUE, json.getBytes());
        ResultActions actions = ResultActionsUtils.postRequestWithTwoMultiPart(mvc, uri, file, data);

        // then
        actions
                .andExpect(status().is4xxClientError())
                .andDo(document("signup-fail1",
                        getRequestPreProcessor(),
                        RequestSnippet.getSignUpMultipartSnippet(),
                        RequestSnippet.getSignUpMultipartDataFieldSnippet()));
    }

    @Test
    @DisplayName("회원 팔로우")
    void memberIntegrationTest10() throws Exception {
        // given
        Member member = MockMember.getMemberByEmailAndNickname("mock@gmail.com", "mock1");
        Member saveMember = memberRepository.save(member);
        Long followingId = saveMember.getId();
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        // when
        String uri = BASE_URL + "/follow/{following-id}";
        ResultActions actions = ResultActionsUtils.postRequestWithTokenAndPathVariable(
                mvc, uri, followingId, accessToken, encryptedRefreshToken);

        // then
        String response = ObjectMapperUtils.actionsSingleToResponseWithData(actions, String.class);
        assertThat(SUCCESS_MEMBER_FOLLOW.getDescription()).isEqualTo(response);
        actions
                .andExpect(status().isOk())
                .andDo(document("member-follow",
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getFollowingPathVariableSnippet()));
    }

    @Test
    @DisplayName("회원 언팔로우")
    void memberIntegrationTest11() throws Exception {
        // given
        Member follower = memberRepository.findByEmail(MockMember.getEmail()).get();
        Member member = MockMember.getMemberByEmailAndNickname("mock@gmail.com", "mock1");
        Member followee = memberRepository.save(member);
        Long followingId = followee.getId();
        Follow follow = MockMember.getFollow(follower, followee);
        followRepository.save(follow);
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        // when
        String uri = BASE_URL + "/unfollow/{following-id}";
        ResultActions actions = ResultActionsUtils.deleteRequestWithTokenAndPathVariable(
                mvc, uri, followingId, accessToken, encryptedRefreshToken);

        // then
        String response = ObjectMapperUtils.actionsSingleToResponseWithData(actions, String.class);
        assertThat(SUCCESS_MEMBER_UNFOLLOW.getDescription()).isEqualTo(response);
        actions
                .andExpect(status().isOk())
                .andDo(document("member-unfollow",
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getFollowingPathVariableSnippet()));
    }
}
