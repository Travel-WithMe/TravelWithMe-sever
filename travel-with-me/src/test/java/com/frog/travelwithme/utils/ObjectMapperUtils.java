package com.frog.travelwithme.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto.Response;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginResponse;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

/**
 * ObjectMapperUtils 설명: 객체 직렬화, 역직렬화
 * 작성자: 이재혁
 * 수정자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/18
 **/
public class ObjectMapperUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static LoginResponse actionsSingleResponseToLoginDto(ResultActions actions) throws Exception {
        String response = resultActionsToResponseAsString(actions);
        return objectMapper.readValue(response, LoginResponse.class);
    }

    public static Response actionsSingleResponseToMemberDto(ResultActions actions) throws Exception {
        String response = resultActionsToResponseAsString(actions);
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, Response.class);
    }

    private static String resultActionsToResponseAsString(ResultActions actions) throws UnsupportedEncodingException {
        String response = actions.andReturn()
                .getResponse()
                .getContentAsString()
                .substring(8);
        return response.substring(0, response.length() - 1);
    }
}
