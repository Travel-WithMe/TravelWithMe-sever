package com.frog.travelwithme.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frog.travelwithme.global.security.auth.controller.dto.AuthDto.LoginResponse;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

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
        return objectMapper.readValue(response, Response.class);
    }

    private static String resultActionsToResponseAsString(ResultActions actions) throws UnsupportedEncodingException {
        String response = actions.andReturn()
                .getResponse()
                .getContentAsString()
                .substring(8);
        response = response.substring(0, response.length() - 1);
        return objectMapper.readValue(response, LoginResponse.class);
    }
}
