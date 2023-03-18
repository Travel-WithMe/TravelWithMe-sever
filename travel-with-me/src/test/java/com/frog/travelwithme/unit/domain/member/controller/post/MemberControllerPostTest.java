package com.frog.travelwithme.unit.domain.member.controller.post;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.unit.domain.member.controller.AbstractMemberController;
import com.frog.travelwithme.utils.ObjectMapperUtils;
import com.frog.travelwithme.utils.reqeust.ResultActionsUtils;
import com.frog.travelwithme.utils.stub.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerPostTest extends AbstractMemberController {

    @Test
    @DisplayName("Test Example Controller")
    void test() throws Exception {

        Member member = StubData.MockMember.getMember();
        String uri = getResourceUrl(1L); // /member/1
        // String json = ObjectMapperUtils.asJsonString(member);
        // ResultActions actions = ResultActionsUtils.postRequest(mvc, uri, json);
    }
}
