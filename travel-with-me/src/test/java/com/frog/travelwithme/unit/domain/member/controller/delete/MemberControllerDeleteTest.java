package com.frog.travelwithme.unit.domain.member.controller.delete;

import com.frog.travelwithme.unit.domain.member.controller.AbstractMemberController;
import com.frog.travelwithme.utils.reqeust.ResultActionsUtils;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/03
 **/
class MemberControllerDeleteTest extends AbstractMemberController {
    @Test
    @DisplayName("Delete member test")
    @WithMockCustomUser
    void deleteMemberTest() throws Exception {
        // given
        doNothing().when(memberService).deleteMember(any());

        // when
        String uri = getResourceUrl(); // /members
        ResultActions actions = ResultActionsUtils.deleteRequest(mvc, uri, userDetails);

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-member",
                        getResponsePreProcessor()));
    }
}
