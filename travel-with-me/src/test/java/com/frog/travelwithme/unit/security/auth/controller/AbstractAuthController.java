package com.frog.travelwithme.unit.security.auth.controller;

import com.frog.travelwithme.global.security.auth.controller.AuthController;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.service.AuthService;
import com.frog.travelwithme.utils.resource.AuthResourceUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/06
 **/
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
public abstract class AbstractAuthController extends AuthResourceUtils {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
}
