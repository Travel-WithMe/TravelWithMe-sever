package com.frog.travelwithme.intergration.buddyrecruitment;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.domain.buddyrecuirtment.repository.BuddyRecruitmentRepository;
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
class BuddyRecruitmentIntegrationTest extends BaseIntegrationTest {

    private final String BASE_URL = "/recruitments";
    private String EMAIL;
    private String EMAIL_OTHER;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AES128Config aes128Config;

    @Autowired
    private BuddyRecruitmentRepository buddyRecruitmentRepository;

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
    void BuddyRecruitmentIntegrationTest1() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        BuddyDto.PostRecruitment postRecruitmentDto = StubData.MockBuddy.getPostRecruitment();

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL)
                .build().toUri().toString();
        String json = ObjectMapperUtils.objectToJsonString(postRecruitmentDto);

        ResultActions actions = ResultActionsUtils.postRequestWithContentAndToken(
                mvc, uri, json, accessToken, encryptedRefreshToken
        );

        // then
        BuddyDto.PostResponseRecruitment response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                BuddyDto.PostResponseRecruitment.class);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo(postRecruitmentDto.getTitle());
        assertThat(response.getContent()).isEqualTo(postRecruitmentDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(postRecruitmentDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(postRecruitmentDto.getTravelStartDate());
        assertThat(response.getTravelEndDate()).isEqualTo(postRecruitmentDto.getTravelEndDate());
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
    void BuddyRecruitmentIntegrationTest2() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        BuddyDto.PatchRecruitment patchRecruitmentDto = StubData.MockBuddy.getPatchRecruitment();

        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        Member writer = memberRepository.findByEmail(EMAIL).get();
        buddyRecruitment.addMember(writer);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/" + saveBuddyRecruitment.getId())
                .build().toUri().toString();
        String json = ObjectMapperUtils.objectToJsonString(patchRecruitmentDto);

        ResultActions actions = ResultActionsUtils.patchRequestWithContentAndToken(
                mvc, uri, json, accessToken, encryptedRefreshToken
        );

        // then
        BuddyDto.PatchResponseRecruitment response = ObjectMapperUtils.actionsSingleToResponseWithData(actions,
                BuddyDto.PatchResponseRecruitment.class);

        assertThat(response.getId()).isEqualTo(saveBuddyRecruitment.getId());
        assertThat(response.getTitle()).isEqualTo(patchRecruitmentDto.getTitle());
        assertThat(response.getContent()).isEqualTo(patchRecruitmentDto.getContent());
        assertThat(response.getTravelNationality()).isEqualTo(patchRecruitmentDto.getTravelNationality());
        assertThat(response.getTravelStartDate()).isEqualTo(patchRecruitmentDto.getTravelStartDate());
        assertThat(response.getTravelEndDate()).isEqualTo(patchRecruitmentDto.getTravelEndDate());

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
    void BuddyRecruitmentIntegrationTest3() throws Exception {
        // given
        CustomUserDetails userDetails = MockMember.getUserDetails();
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        BuddyRecruitment buddyRecruitment = StubData.MockBuddy.getBuddyRecruitment();
        Member writer = memberRepository.findByEmail(EMAIL).get();
        buddyRecruitment.addMember(writer);
        BuddyRecruitment saveBuddyRecruitment = buddyRecruitmentRepository.save(buddyRecruitment);

        // when
        String uri = UriComponentsBuilder.newInstance()
                .path(BASE_URL + "/" + saveBuddyRecruitment.getId() + "/" + "deleted")
                .build().toUri().toString();

        ResultActions actions = ResultActionsUtils.postRequestWithToken(
                mvc, uri, accessToken, encryptedRefreshToken
        );


        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-recruitment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor()
                ));
    }
}
