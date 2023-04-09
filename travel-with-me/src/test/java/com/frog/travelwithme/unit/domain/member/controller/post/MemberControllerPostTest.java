package com.frog.travelwithme.unit.domain.member.controller.post;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.unit.domain.member.controller.AbstractMemberController;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.reqeust.RequestPostSnippet;
import com.frog.travelwithme.utils.reqeust.ResultActionsUtils;
import com.frog.travelwithme.utils.response.ResponsePostSnippet;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.1
 * 작성일자: 2023/04/03
 **/
@Slf4j
class MemberControllerPostTest extends AbstractMemberController {
    @Test
    @DisplayName("Sign up test")
    @WithMockCustomUser
    void signUpTest() throws Exception {
        // given
        MemberDto.SignUp signUpDto = StubData.MockMember.getSignUpDto();
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.signUp(any(MemberDto.SignUp.class))).willReturn(response);

        // when
        String uri = getResourceUrl("signup"); // /members/signup
        String json = ObjectMapperUtils.asJsonString(signUpDto);
        ResultActions actions = ResultActionsUtils.postRequest(mvc, uri, json);

        // then
        actions
                .andExpect(status().isCreated())
                .andDo(document("signup",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestPostSnippet.getSignUpSnippet(),
                        ResponsePostSnippet.getMemberResponseSnippet()));
    }
}
