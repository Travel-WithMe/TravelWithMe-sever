package com.frog.travelwithme.intergration.buddy;

import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.MatchingRepository;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.exception.ErrorResponse;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.intergration.BaseIntegrationTest;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.StubData.MockMember;
import com.frog.travelwithme.utils.snippet.reqeust.RequestSnippet;
import com.frog.travelwithme.utils.snippet.response.ErrorResponseSnippet;
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
class RecruitmentIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/recruitments";
    private String EMAIL;
    private String EMAIL_OTHER_ONE;
    private String EMAIL_OTHER_TWO;

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
        MemberDto.SignUp memberOne = MockMember.getSignUpDto();
        memberService.signUp(memberOne);
        EMAIL = memberOne.getEmail();

        // dhfif718@gmail.com 회원 추가
        MemberDto.SignUp memberTwo = MockMember.getSignUpDtoByEmailAndNickname(
                "dhfif718@gmail.com",
                "이재혁"
        );
        memberService.signUp(memberTwo);
        EMAIL_OTHER_ONE = memberTwo.getEmail();

        // kkd718@naver.com 회원 추가
        MemberDto.SignUp memberThree = MockMember.getSignUpDtoByEmailAndNickname(
                "kkd718@naver.com",
                "리젤란"
        );
        memberService.signUp(memberThree);
        EMAIL_OTHER_TWO = memberThree.getEmail();
    }

    @Test
    @DisplayName("동행 모집글 작성 테스트")
    void recruitmentIntegrationTest1() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        RecruitmentDto.Post postDto = StubData.MockRecruitment.getPostRecruitment();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions = ResultActionsUtils.postRequestWithContentAndToken(
                mvc, uri, json, accessToken, encryptedRefreshToken
        );

        // then
        RecruitmentDto.PostResponse response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                RecruitmentDto.PostResponse.class);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo(postDto.getTitle());
        assertThat(response.getContent()).isEqualTo(postDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(postDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(postDto.getTravelStartDate());
        assertThat(response.getTravelEndDate()).isEqualTo(postDto.getTravelEndDate());
        assertThat(response.getViewCount()).isEqualTo(0L);
        assertThat(response.getCommentCount()).isEqualTo(0L);
        assertThat(response.getNickname()).isEqualTo("nickname");
        assertThat(response.getMemberImage()).isEqualTo(MockMember.getImage());


        actions
                .andExpect(status().isCreated())
                .andDo(document("post-recruitment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getPostRecruitmentSnippet(),
                        ResponseSnippet.getPostRecruitmentSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 수정 테스트")
    void recruitmentIntegrationTest2() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        RecruitmentDto.Patch patchDto = StubData.MockRecruitment.getPatchRecruitment();

        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = memberRepository.findByEmail(EMAIL).get();
        recruitment.addMember(writer);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = savedRecruitment.getId();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + recruitmentId)
                .build().toUri().toString();
        String json = ObjectMapperUtils.objectToJsonString(patchDto);

        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndToken(
                mvc, uri, json, accessToken, encryptedRefreshToken
        );

        // then
        RecruitmentDto.PatchResponse response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                RecruitmentDto.PatchResponse.class);

        assertThat(response.getId()).isEqualTo(savedRecruitment.getId());
        assertThat(response.getTitle()).isEqualTo(patchDto.getTitle());
        assertThat(response.getContent()).isEqualTo(patchDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(patchDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(patchDto.getTravelStartDate());
        assertThat(response.getTravelEndDate()).isEqualTo(patchDto.getTravelEndDate());

        actions
                .andExpect(status().isOk())
                .andDo(document("patch-recruitment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getPatchRecruitmentSnippet(),
                        ResponseSnippet.getPatchRecruitmentSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 삭제 테스트")
    void recruitmentIntegrationTest3() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Member writer = memberRepository.findByEmail(EMAIL).get();
        recruitment.addMember(writer);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = savedRecruitment.getId();

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + recruitmentId)
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.deleteRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );


        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("delete-recruitment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()
                ));
    }

    @Test
    @DisplayName("동행 모집글 매칭신청 회원 리스트 조회")
    void recruitmentIntegrationTest4() throws Exception {
        // given
        Member writer = memberRepository.findByEmail(EMAIL).get();
        Member user1 = memberRepository.findByEmail(EMAIL_OTHER_ONE).get();
        Member user2 = memberRepository.findByEmail(EMAIL_OTHER_TWO).get();

        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = savedRecruitment.getId();

        Matching matching1 = StubData.MockMatching.getMatching();
        matching1.addMember(user1);
        matching1.addRecruitment(savedRecruitment);
        Matching matching2 = StubData.MockMatching.getMatching();
        matching2.addMember(user2);
        matching2.addRecruitment(savedRecruitment);

        savedRecruitment.addMatching(matching1);
        savedRecruitment.addMatching(matching2);

        matchingRepository.save(matching1);
        matchingRepository.save(matching2);

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + recruitmentId + "/" + "matching-request-member-list")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequest(
                mvc, uri
        );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-matching-request-member-list-recruitment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getMatchingMemberListSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 매칭신청 회원 리스트 조회 (모집이 종료된 게시글)")
    void recruitmentIntegrationTest5() throws Exception {
        // given
        Member writer = memberRepository.findByEmail(EMAIL).get();
        Member user1 = memberRepository.findByEmail(EMAIL_OTHER_ONE).get();
        Member user2 = memberRepository.findByEmail(EMAIL_OTHER_TWO).get();

        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = savedRecruitment.getId();

        Matching matching1 = StubData.MockMatching.getMatching();
        matching1.addMember(user1);
        matching1.addRecruitment(savedRecruitment);
        Matching matching2 = StubData.MockMatching.getMatching();
        matching2.addMember(user2);
        matching2.addRecruitment(savedRecruitment);

        savedRecruitment.addMatching(matching1);
        savedRecruitment.addMatching(matching2);
        savedRecruitment.end();

        matchingRepository.save(matching1);
        matchingRepository.save(matching2);


        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + recruitmentId + "/" + "matching-request-member-list")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequest(
                mvc, uri
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getMessage());
        actions
                .andDo(document("get-matching-request-member-list-recruitment-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 매칭신청 회원 리스트 조회 (동행 모집글이 없음)")
    void recruitmentIntegrationTest6() throws Exception {
        // given
        Long recruitmentId = 1L;

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + recruitmentId + "/" + "matching-request-member-list")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequest(
                mvc, uri
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus())
                .isEqualTo(ExceptionCode.RECRUITMENT_MATCHING_REQUEST_MEMBER_NOT_FOUND.getStatus());
        assertThat(response.getMessage())
                .isEqualTo(ExceptionCode.RECRUITMENT_MATCHING_REQUEST_MEMBER_NOT_FOUND.getMessage());
        actions
                .andDo(document("get-matching-request-member-list-recruitment-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 매칭승인 회원 리스트 조회")
    void recruitmentIntegrationTest7() throws Exception {
        // given
        Member writer = memberRepository.findByEmail(EMAIL).get();
        Member user1 = memberRepository.findByEmail(EMAIL_OTHER_ONE).get();
        Member user2 = memberRepository.findByEmail(EMAIL_OTHER_TWO).get();

        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = savedRecruitment.getId();

        Matching matching1 = StubData.MockMatching.getMatching();
        matching1.addMember(user1);
        matching1.addRecruitment(savedRecruitment);
        matching1.approve();
        Matching matching2 = StubData.MockMatching.getMatching();
        matching2.addMember(user2);
        matching2.addRecruitment(savedRecruitment);
        matching2.approve();

        savedRecruitment.addMatching(matching1);
        savedRecruitment.addMatching(matching2);

        matchingRepository.save(matching1);
        matchingRepository.save(matching2);

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + recruitmentId + "/" + "matching-approved-member-list")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequest(
                mvc, uri
        );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-matching-approved-member-list-recruitment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ResponseSnippet.getMatchingMemberListSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 매칭승인 회원 리스트 조회 (모집이 종료된 게시글)")
    void recruitmentIntegrationTest8() throws Exception {
        // given
        Member writer = memberRepository.findByEmail(EMAIL).get();
        Member user1 = memberRepository.findByEmail(EMAIL_OTHER_ONE).get();
        Member user2 = memberRepository.findByEmail(EMAIL_OTHER_TWO).get();

        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        recruitment.addMember(writer);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        Long recruitmentId = savedRecruitment.getId();

        Matching matching1 = StubData.MockMatching.getMatching();
        matching1.addMember(user1);
        matching1.addRecruitment(savedRecruitment);
        matching1.approve();
        Matching matching2 = StubData.MockMatching.getMatching();
        matching2.addMember(user2);
        matching2.addRecruitment(savedRecruitment);
        matching2.approve();

        savedRecruitment.addMatching(matching1);
        savedRecruitment.addMatching(matching2);
        savedRecruitment.end();

        matchingRepository.save(matching1);
        matchingRepository.save(matching2);

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + recruitmentId + "/" + "matching-approved-member-list")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequest(
                mvc, uri
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.RECRUITMENT_EXPIRED.getMessage());
        actions
                .andDo(document("get-matching-approved-member-list-recruitment-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 매칭승인 회원 리스트 조회 (동행 모집글이 없음)")
    void recruitmentIntegrationTest9() throws Exception {
        // given
        Long recruitmentId = 1L;

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + recruitmentId + "/" + "matching-approved-member-list")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.getRequest(
                mvc, uri
        );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus())
                .isEqualTo(ExceptionCode.RECRUITMENT_MATCHING_REQUEST_MEMBER_NOT_FOUND.getStatus());
        assertThat(response.getMessage())
                .isEqualTo(ExceptionCode.RECRUITMENT_MATCHING_REQUEST_MEMBER_NOT_FOUND.getMessage());
        actions
                .andDo(document("get-matching-approved-member-list-recruitment-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }
}
