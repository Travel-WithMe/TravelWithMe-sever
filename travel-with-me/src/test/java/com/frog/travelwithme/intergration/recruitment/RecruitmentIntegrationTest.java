package com.frog.travelwithme.intergration.recruitment;

import com.frog.travelwithme.domain.recruitment.controller.dto.RecruitmentDto;
import com.frog.travelwithme.domain.recruitment.entity.Recruitment;
import com.frog.travelwithme.domain.recruitment.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.intergration.BaseIntegrationTest;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.StubData.MockMember;
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
class RecruitmentIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/recruitments";
    private String EMAIL;
    private String EMAIL_OTHER;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

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
        EMAIL_OTHER = memberTwo.getEmail();
    }

    @Test
    @DisplayName("동행 작성 테스트")
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
    @DisplayName("동행 수정 테스트")
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
        Recruitment saveBuddy = recruitmentRepository.save(recruitment);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + saveBuddy.getId())
                .build().toUri().toString();
        String json = ObjectMapperUtils.objectToJsonString(patchDto);

        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndToken(
                mvc, uri, json, accessToken, encryptedRefreshToken
        );

        // then
        RecruitmentDto.PatchResponse response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                RecruitmentDto.PatchResponse.class);

        assertThat(response.getId()).isEqualTo(saveBuddy.getId());
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
    @DisplayName("동행 삭제 테스트")
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
        Recruitment saveBuddy = recruitmentRepository.save(recruitment);

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + saveBuddy.getId())
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
}
