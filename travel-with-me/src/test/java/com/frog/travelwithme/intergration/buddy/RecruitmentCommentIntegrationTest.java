package com.frog.travelwithme.intergration.buddy;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.frog.travelwithme.domain.buddy.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentCommentRepository;
import com.frog.travelwithme.domain.buddy.service.RecruitmentService;
import com.frog.travelwithme.domain.common.comment.dto.CommentDto;
import com.frog.travelwithme.domain.feed.service.FeedService;
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
import com.frog.travelwithme.utils.snippet.reqeust.RequestSnippet;
import com.frog.travelwithme.utils.snippet.response.ErrorResponseSnippet;
import com.frog.travelwithme.utils.snippet.response.ResponseSnippet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class RecruitmentCommentIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/recruitments";
    private final String SUB_URL = "/comments";
    private String EMAIL;
    private String EMAIL_OTHER_ONE;
    private String EMAIL_OTHER_TWO;
    private Long RECRUITMENT_ID;
    private Long COMMENT_ID;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecruitmentCommentRepository recruitmentCommentRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private RecruitmentService recruitmentService;

    @Autowired
    private AmazonS3 amazonS3;

    @BeforeEach
    void beforeEach() throws Exception {
        // Mock S3 시나리오 설정
        given(amazonS3.putObject(any(PutObjectRequest.class))).willReturn(new PutObjectResult());
        given(amazonS3.getUrl(any(), any())).willReturn(
                new URL(StubData.CustomMultipartFile.getIMAGE_URL()));

        List<MultipartFile> files = StubData.CustomMultipartFile.getMultipartFiles();

        // e_ma-il@gmail.com 회원 추가
        MemberDto.SignUp writer = StubData.MockMember.getSignUpDto();
        memberService.signUp(writer);
        EMAIL = writer.getEmail();

        // dhfif718@gmail.com 회원 추가
        MemberDto.SignUp memberOne = StubData.MockMember.getSignUpDtoByEmailAndNickname(
                "dhfif718@gmail.com",
                "이재혁"
        );
        memberService.signUp(memberOne);
        EMAIL_OTHER_ONE = memberOne.getEmail();

        // kkd718@naver.com 회원 추가
        MemberDto.SignUp memberTwo = StubData.MockMember.getSignUpDtoByEmailAndNickname(
                "kkd718@naver.com",
                "리젤란"
        );
        memberService.signUp(memberTwo);
        EMAIL_OTHER_TWO = memberTwo.getEmail();

        // 동행 모집글 추가 = 작성자(e_ma-il@gmail.com)
        BuddyDto.RecruitmentPost recruitmentPostRecruitmentDto = StubData.MockRecruitment.getPostRecruitment();
        RECRUITMENT_ID = recruitmentService.createRecruitmentByEmail(recruitmentPostRecruitmentDto, EMAIL).getId();

        // 기준 댓글 추가
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        RecruitmentComment savedRecruitmentComment = recruitmentCommentRepository.save(recruitmentComment);
        COMMENT_ID = savedRecruitmentComment.getId();

    }

    @Test
    @DisplayName("동행 모집글 댓글 작성 (회원태그 사용)")
    void RecruitmentCommentIntegrationTest1() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Long groupId = null;
        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long memberId = member.getId();
        Long recruitmentId = RECRUITMENT_ID;
        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(1, groupId , memberId);

        // when
        String uri = BASE_URL + "/{recruitment-id}" + SUB_URL;
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions =
                ResultActionsUtils.postRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, recruitmentId, json, accessToken, encryptedRefreshToken
                );

        // then
        CommentDto.PostResponse response =
                ObjectMapperUtils.actionsSingleToResponseWithData(actions, CommentDto.PostResponse.class);

        assertThat(response.getDepth()).isEqualTo(1);
        assertThat(response.getContent()).isEqualTo(postDto.getContent());
        assertThat(response.getTaggedMemberId()).isEqualTo(postDto.getTaggedMemberId());
        actions
                .andExpect(status().isCreated())
                .andDo(document("post-recruitment-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getRecruitmentPathVariableSnippet(),
                        RequestSnippet.getPostCommentSnippet(),
                        ResponseSnippet.getPostCommentSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 댓글 작성 (회원태그 미사용)")
    void RecruitmentCommentIntegrationTest2() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Long groupId = null;
        Long memberId = null;
        Long recruitmentId = RECRUITMENT_ID;
        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(1, groupId , memberId);

        // when
        String uri = BASE_URL + "/{recruitment-id}" + SUB_URL;
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions =
                ResultActionsUtils.postRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, recruitmentId, json, accessToken, encryptedRefreshToken
                );

        // then
        CommentDto.PostResponse response =
                ObjectMapperUtils.actionsSingleToResponseWithData(actions, CommentDto.PostResponse.class);

        assertThat(response.getDepth()).isEqualTo(1);
        assertThat(response.getContent()).isEqualTo(postDto.getContent());
        assertThat(response.getTaggedMemberId()).isEqualTo(postDto.getTaggedMemberId());
        actions
                .andExpect(status().isCreated())
                .andDo(document("post-recruitment-comment-no-tagged",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getRecruitmentPathVariableSnippet(),
                        RequestSnippet.getPostCommentSnippet(),
                        ResponseSnippet.getPostCommentSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 댓글 작성 (예외: GroupId가 있는 경우)")
    void RecruitmentCommentIntegrationTest3() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Long groupId = COMMENT_ID;
        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long memberId = member.getId();
        Long recruitmentId = RECRUITMENT_ID;
        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(1, groupId , memberId);

        // when
        String uri = BASE_URL + "/{recruitment-id}" + SUB_URL;
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions =
                ResultActionsUtils.postRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, recruitmentId, json, accessToken, encryptedRefreshToken
                );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.COMMENT_DO_NOT_NEED_GROUP_ID.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.COMMENT_DO_NOT_NEED_GROUP_ID.getMessage());

        actions
                .andExpect(status().is4xxClientError())
                .andDo(document("post-recruitment-comment-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getRecruitmentPathVariableSnippet(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 댓글 작성 (예외: 태그된 멤버가 존재하지 않는 경우)")
    void RecruitmentCommentIntegrationTest4() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long groupId = null;
        Long memberId = member.getId() - 2; // 태그된 멤버가 존재하지 않는 경우
        Long recruitmentId = RECRUITMENT_ID;
        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(1, groupId , memberId);

        // when
        String uri = BASE_URL + "/{recruitment-id}" + SUB_URL;
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions =
                ResultActionsUtils.postRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, recruitmentId, json, accessToken, encryptedRefreshToken
                );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.TAGGED_MEMBER_NOT_FOUND.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.TAGGED_MEMBER_NOT_FOUND.getMessage());
        actions
                .andExpect(status().is4xxClientError())
                .andDo(document("post-recruitment-comment-exception-2",
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getRecruitmentPathVariableSnippet(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 대댓글 작성")
    void RecruitmentCommentIntegrationTest5() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long groupId = COMMENT_ID;
        Long memberId = member.getId();
        Long recruitmentId = RECRUITMENT_ID;
        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(2, groupId , memberId);

        // when
        String uri = BASE_URL + "/{recruitment-id}" + SUB_URL;
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions =
                ResultActionsUtils.postRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, recruitmentId, json, accessToken, encryptedRefreshToken
                );

        // then
        CommentDto.PostResponse response =
                ObjectMapperUtils.actionsSingleToResponseWithData(actions, CommentDto.PostResponse.class);

        assertThat(response.getDepth()).isEqualTo(2);
        assertThat(response.getContent()).isEqualTo(postDto.getContent());
        assertThat(response.getTaggedMemberId()).isEqualTo(postDto.getTaggedMemberId());

        actions
                .andExpect(status().isCreated())
                .andDo(document("post-recruitment-comment-reply",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getRecruitmentPathVariableSnippet(),
                        RequestSnippet.getPostCommentSnippet(),
                        ResponseSnippet.getPostCommentSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 대댓글 작성 (예외: 기준댓글이 없는경우)")
    void RecruitmentCommentIntegrationTest6() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long groupId = COMMENT_ID - 1; // 없는 댓글
        Long memberId = member.getId();
        Long recruitmentId = RECRUITMENT_ID;
        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(2, groupId , memberId);

        // when
        String uri = BASE_URL + "/{recruitment-id}" + SUB_URL;
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions =
                ResultActionsUtils.postRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, recruitmentId, json, accessToken, encryptedRefreshToken
                );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.COMMENT_REPLY_GROUP_ID_NOT_FOUND.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.COMMENT_REPLY_GROUP_ID_NOT_FOUND.getMessage());
        actions
                .andExpect(status().is4xxClientError())
                .andDo(document("post-recruitment-comment-reply-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getRecruitmentPathVariableSnippet(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 대댓글 작성 (예외: GroupId가 없는 경우)")
    void RecruitmentCommentIntegrationTest7() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long groupId = null;
        Long memberId = member.getId();
        Long recruitmentId = RECRUITMENT_ID;
        CommentDto.Post postDto =
                StubData.MockComment.getPostDtoByDepthAndGroupIdAndTaggedMemberId(2, groupId , memberId);

        // when
        String uri = BASE_URL + "/{recruitment-id}" + SUB_URL;
        String json = ObjectMapperUtils.objectToJsonString(postDto);

        ResultActions actions =
                ResultActionsUtils.postRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, recruitmentId, json, accessToken, encryptedRefreshToken
                );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.COMMENT_REPLY_NEED_GROUP_ID.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.COMMENT_REPLY_NEED_GROUP_ID.getMessage());

        actions
                .andExpect(status().is4xxClientError())
                .andDo(document("post-recruitment-comment-reply-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getRecruitmentPathVariableSnippet(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 댓글 수정 (회원태그 사용)")
    void RecruitmentCommentIntegrationTest8() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberService.findMember(EMAIL);
        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long memberId = member.getId();
        RecruitmentComment recruitmentComment = recruitmentCommentRepository.findById(COMMENT_ID).get();
        recruitmentComment.addMember(writer);
        Long commentId = recruitmentComment.getId();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경완료", memberId);

        // when
        String uri = BASE_URL + SUB_URL + "/{comment-id}" ;
        String json = ObjectMapperUtils.objectToJsonString(patchDto);

        ResultActions actions =
                ResultActionsUtils.patchRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, commentId, json, accessToken, encryptedRefreshToken
                );

        // then
        CommentDto.PatchResponse response =
                ObjectMapperUtils.actionsSingleToResponseWithData(actions, CommentDto.PatchResponse.class);
        RecruitmentComment findRecruitmentComment = recruitmentCommentRepository.findById(commentId).get();

        assertThat(response.getCommentId()).isEqualTo(commentId);
        assertThat(response.getContent()).isEqualTo(findRecruitmentComment.getContent());
        assertThat(response.getTaggedMemberId()).isEqualTo(findRecruitmentComment.getTaggedMemberId());

        actions
                .andExpect(status().isOk())
                .andDo(document("patch-recruitment-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getCommentPathVariableSnippet(),
                        RequestSnippet.getPatchCommentSnippet(),
                        ResponseSnippet.getPatchCommentSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 댓글 수정 (회원태그 미사용)")
    void RecruitmentCommentIntegrationTest9() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member writer = memberService.findMember(EMAIL);
        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        RecruitmentComment recruitmentComment = recruitmentCommentRepository.findById(COMMENT_ID).get();
        recruitmentComment.addMember(writer);
        Long commentId = recruitmentComment.getId();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경완료", null);

        // when
        String uri = BASE_URL + SUB_URL + "/{comment-id}" ;
        String json = ObjectMapperUtils.objectToJsonString(patchDto);

        ResultActions actions =
                ResultActionsUtils.patchRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, commentId, json, accessToken, encryptedRefreshToken
                );

        // then
        CommentDto.PatchResponse response =
                ObjectMapperUtils.actionsSingleToResponseWithData(actions, CommentDto.PatchResponse.class);
        RecruitmentComment findRecruitmentComment = recruitmentCommentRepository.findById(commentId).get();

        assertThat(response.getCommentId()).isEqualTo(commentId);
        assertThat(response.getContent()).isEqualTo(findRecruitmentComment.getContent());
        assertThat(response.getTaggedMemberId()).isEqualTo(findRecruitmentComment.getTaggedMemberId());
        assertThat(response.getTaggedMemberId()).isEqualTo(1);

        actions
                .andExpect(status().isOk())
                .andDo(document("patch-recruitment-comment-no-tagged",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getCommentPathVariableSnippet(),
                        RequestSnippet.getPatchCommentSnippet(),
                        ResponseSnippet.getPatchCommentSnippet()
                ));
    }

    @Test
    @DisplayName("동행 모집글 댓글 수정 (예외: 댓글이 존재하지 않는경우)")
    void RecruitmentCommentIntegrationTest10() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long commentId = COMMENT_ID + 1;
        Long memberId = member.getId();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경완료", memberId);

        // when
        String uri = BASE_URL + SUB_URL + "/{comment-id}" ;
        String json = ObjectMapperUtils.objectToJsonString(patchDto);

        ResultActions actions =
                ResultActionsUtils.patchRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, commentId, json, accessToken, encryptedRefreshToken
                );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.COMMENT_NOT_FOUND.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.COMMENT_NOT_FOUND.getMessage());

        actions
                .andExpect(status().is4xxClientError())
                .andDo(document("patch-recruitment-comment-exception-1",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getCommentPathVariableSnippet(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }

    @Test
    @DisplayName("동행 모집글 댓글 수정 (예외: 댓글이 작성자가 일치하지 않는 경우)")
    void RecruitmentCommentIntegrationTest11() throws Exception {
        // given
        CustomUserDetails userDetails = StubData.MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        Member member = memberService.findMember(EMAIL_OTHER_ONE);
        Long memberId = member.getId();
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        recruitmentComment.addMember(member);
        RecruitmentComment savedRecruitmentComment = recruitmentCommentRepository.save(recruitmentComment);
        Long commentId = savedRecruitmentComment.getId();
        CommentDto.Patch patchDto =
                StubData.MockComment.getPatchDtoByContentAndTaggedMemberId("변경완료", memberId);

        // when
        String uri = BASE_URL + SUB_URL + "/{comment-id}" ;
        String json = ObjectMapperUtils.objectToJsonString(patchDto);

        ResultActions actions =
                ResultActionsUtils.patchRequestWithTokenAndPathVariableAndContent(
                        mvc, uri, commentId, json, accessToken, encryptedRefreshToken
                );

        // then
        ErrorResponse response = ObjectMapperUtils.actionsSingleToResponse(actions, ErrorResponse.class);

        assertThat(response.getStatus()).isEqualTo(ExceptionCode.COMMENT_WRITER_NOT_MATCH.getStatus());
        assertThat(response.getMessage()).isEqualTo(ExceptionCode.COMMENT_WRITER_NOT_MATCH.getMessage());

        actions
                .andExpect(status().is4xxClientError())
                .andDo(document("patch-recruitment-comment-exception-2",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestSnippet.getTokenSnippet(),
                        RequestSnippet.getCommentPathVariableSnippet(),
                        ErrorResponseSnippet.getFieldErrorSnippetsLong()
                ));
    }
}
