package com.frog.travelwithme.unit.domain.member.controller.patch;

import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.unit.domain.member.controller.AbstractMemberController;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.StubData;
import com.frog.travelwithme.utils.reqeust.RequestPostSnippet;
import com.frog.travelwithme.utils.reqeust.ResultActionsUtils;
import com.frog.travelwithme.utils.response.ResponsePostSnippet;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
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
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/03
 **/
class MemberControllerPatchTest extends AbstractMemberController {
    @Test
    @DisplayName("Patch member test")
    @WithMockCustomUser(email = "email", password = "password")
    void patchMemberTest() throws Exception {
        // given
        MemberDto.Patch patchDto = StubData.MockMember.getPatchDto();
        MemberDto.Response response = StubData.MockMember.getResponseDto();
        given(memberService.updateMember(any(MemberDto.Patch.class), any())).willReturn(response);

        // when
        String json = ObjectMapperUtils.asJsonString(patchDto);
        String uri = getResourceUrl(); // /members
        ResultActions actions = ResultActionsUtils.patchRequest(mvc, uri, json, userDetails);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        RequestPostSnippet.getPatchSnippet(),
                        ResponsePostSnippet.getMemberResponseSnippet()));
    }
}
