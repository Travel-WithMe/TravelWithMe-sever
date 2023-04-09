package com.frog.travelwithme.unit.domain.member.controller.get;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.unit.domain.member.controller.AbstractMemberController;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.reqeust.ResultActionsUtils;
import com.frog.travelwithme.utils.response.ResponsePostSnippet;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/03
 **/
class MemberControllerGetTest extends AbstractMemberController {
    @Test
    @DisplayName("Get member test")
    @WithMockCustomUser
    void getMemberTest() throws Exception {
        // given
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.findMemberByEmail(any())).willReturn(response);

        // when
        String uri = getResourceUrl();
        ResultActions actions = ResultActionsUtils.getRequest(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-member",
                        getResponsePreProcessor(),
                        ResponsePostSnippet.getMemberResponseSnippet()));
    }
}
