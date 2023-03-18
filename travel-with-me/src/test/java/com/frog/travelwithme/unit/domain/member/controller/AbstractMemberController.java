package com.frog.travelwithme.unit.domain.member.controller;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.utils.resource.CommunityResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

//@Import(MapStructImpl.class)
@AutoConfigureRestDocs
//@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public abstract class AbstractMemberController extends CommunityResourceUtils {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected Member member;

}
