package com.frog.travelwithme.unit.security.auth.controller;

import com.frog.travelwithme.global.security.auth.controller.MailController;
import com.frog.travelwithme.global.security.auth.service.MailService;
import com.frog.travelwithme.utils.ResultActionsUtils;
import com.frog.travelwithme.utils.security.WithMockCustomUser;
import com.frog.travelwithme.utils.snippet.reqeust.RequestSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;

import static com.frog.travelwithme.utils.ApiDocumentUtils.getRequestPreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = MailController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
@AutoConfigureRestDocs
class MailControllerTest {

    private static final String BASE_URL = "/emails";

    private static final String EMAIL_KEY = "email";

    private static final String EMAIL_VALUE = "email@gmail.com";

    private static final String CODE_KEY = "code";

    private static final String CODE_VALUE = "123456";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MailService mailService;

    @Test
    @DisplayName("메일을 전송합니다")
    @WithMockCustomUser
    void mailControllerTest1() throws Exception {
        // given
        MultiValueMap<String, String> papram = new LinkedMultiValueMap<>();
        papram.add(EMAIL_KEY, EMAIL_VALUE);
        doNothing().when(mailService).sendEmail(Mockito.any());

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/verification-requests")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithParams(mvc, uri, papram);

        // then
        actions.andExpect(status().isOk())
                .andDo(document("email-verification-request",
                        getRequestPreProcessor(),
                        RequestSnippet.getMailVerificiationRequestSnippet()));
    }

    @Test
    @DisplayName("인증 번호를 통해 메일을 인증합니다")
    @WithMockCustomUser
    void mailControllerTest2() throws Exception {
        // given
        MultiValueMap<String, String> emailPapram = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> codePapram = new LinkedMultiValueMap<>();
        emailPapram.add(EMAIL_KEY, EMAIL_VALUE);
        codePapram.add(CODE_KEY, CODE_VALUE);
        given(mailService.verifiedCode(Mockito.any(), Mockito.any())).willReturn(true);

        // when
        String uri = UriComponentsBuilder.newInstance().path(BASE_URL + "/verification-requests")
                .build().toUri().toString();
        ResultActions actions = ResultActionsUtils.getRequestWithTwoParams(mvc, uri, emailPapram, codePapram);

        // then
        actions.andExpect(status().isOk())
                .andDo(document("email-verification-request",
                        getRequestPreProcessor(),
                        RequestSnippet.getMailVerificiationSnippet()));
    }
}
