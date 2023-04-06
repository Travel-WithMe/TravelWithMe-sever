package com.frog.travelwithme.unit.domain.member.controller;

import com.frog.travelwithme.domain.member.controller.MemberController;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.service.MemberService;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.utils.resource.MemberResourceUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
public abstract class AbstractMemberController extends MemberResourceUtils {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected Member member;

    @Mock
    protected CustomUserDetails userDetails;
}
