package com.frog.travelwithme.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.global.dto.MessageResponseDto;
import com.frog.travelwithme.global.dto.SingleResponseDto;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginResponse;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

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

    public static String objectToJsonString(Object obj) throws JsonProcessingException {
        return objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(obj);
    }

    public static <T> String actionsSingleToString(ResultActions actions, Class<T> responseClass) throws Exception {
        String response = resultActionsToResponseAsString(actions);
        String className = responseClass.getName();
        String substring = null;

        if(className.equals(SingleResponseDto.class.getName())) {
            substring = response.substring(8, response.length() - 1);
        } else if(className.equals(MessageResponseDto.class.getName())) {
            substring = response.substring(12, response.length() - 2);
        }
        return substring;
    }

    public static <T> T actionsSingleToResponse(ResultActions actions, Class<T> responseClass) throws Exception {
        String response = resultActionsToResponseAsString(actions);
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, responseClass);
    }

    public static <T> T actionsSingleToResponseWithData(ResultActions actions, Class<T> responseClass) throws Exception {
        String response = resultActionsToResponseAsStringWithData(actions);
        return objectMapper.registerModule(new JavaTimeModule()).readValue(response, responseClass);
    }

    private static String resultActionsToResponseAsString(ResultActions actions) throws UnsupportedEncodingException {
        return actions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    private static String resultActionsToResponseAsStringWithData(ResultActions actions) throws UnsupportedEncodingException {
        String response = actions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8)
                .substring(8);
        return response.substring(0, response.length() - 1);
    }
}
