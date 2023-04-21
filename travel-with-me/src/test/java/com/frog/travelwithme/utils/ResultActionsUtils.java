package com.frog.travelwithme.utils;

import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * ResultActionsUtils 설명: ResultActions 관리
 * 작성자: 이재혁
 * 수정자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/18
 **/
public class ResultActionsUtils {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String REFRESH_HEADER = "Refresh";
    private static final String BEARER_PREFIX = "Bearer ";

    public static ResultActions postRequest(MockMvc mockMvc,
                                            String url) throws Exception {
        return mockMvc.perform(post(url)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequestWithHeaders(MockMvc mockMvc,
                                                       String url,
                                                       HttpHeaders headers) throws Exception {
        return mockMvc.perform(post(url)
                        .headers(headers)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequestWithContent(MockMvc mockMvc,
                                                       String url,
                                                       String json) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequestWithContentAndUserDetails(MockMvc mockMvc,
                                                                     String url,
                                                                     String json,
                                                                     CustomUserDetails userDetails) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
                        .with(user(userDetails)))
                .andDo(print());
    }

    public static ResultActions postRequestWithContentAndHeaders(MockMvc mockMvc,
                                                                 String url,
                                                                 String json,
                                                                 HttpHeaders headers) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .headers(headers)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequestWithContentAndToken(MockMvc mockMvc,
                                                               String url,
                                                               String json,
                                                               String accessToken,
                                                               String encryptedRefreshToken
    ) throws Exception {

        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequest(MockMvc mockMvc,
                                             String url) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions patchRequestWithToken(MockMvc mockMvc,
                                                      String url,
                                                      String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequestWithToken(MockMvc mockMvc,
                                                      String url,
                                                      String accessToken,
                                                      String encryptedRefreshToken
    ) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequestWithContentAndToken(MockMvc mockMvc,
                                                                String url,
                                                                String json,
                                                                String accessToken,
                                                                String encryptedRefreshToken
    ) throws Exception {

        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequestWithContentAndUserDetails(MockMvc mockMvc,
                                                                      String url,
                                                                      String json,
                                                                      CustomUserDetails userDetails) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
                        .with(user(userDetails)))
                .andDo(print());
    }

    public static ResultActions deleteRequest(MockMvc mockMvc,
                                              String url) throws Exception {
        return mockMvc.perform(delete(url)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions deleteRequestWithUserDetails(MockMvc mockMvc,
                                                             String url,
                                                             CustomUserDetails userDetails) throws Exception {
        return mockMvc.perform(delete(url)
                        .with(csrf())
                        .with(user(userDetails)))
                .andDo(print());
    }

    public static ResultActions deleteRequestWithToken(MockMvc mockMvc,
                                                       String url,
                                                       String accessToken,
                                                       String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions getRequest(MockMvc mockMvc,
                                           String url) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    public static ResultActions getRequestWithContent(MockMvc mockMvc,
                                                      String url,
                                                      String json) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print());
    }

    public static ResultActions getRequestWithUserDetails(MockMvc mockMvc,
                                                          String url,
                                                          CustomUserDetails userDetails) throws Exception {
        return mockMvc.perform(get(url)
                        .with(user(userDetails)))
                .andDo(print());
    }

    public static ResultActions getRequestWithToken(MockMvc mockMvc,
                                                    String url,
                                                    String accessToken,
                                                    String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions getRequestWithParams(MockMvc mockMvc,
                                                     String url,
                                                     MultiValueMap<String, String> paprams) throws Exception {
        return mockMvc.perform(get(url)
                        .params(paprams))
                .andDo(print());
    }

    public static ResultActions getRequestWithTwoParams(MockMvc mockMvc,
                                                        String url,
                                                        MultiValueMap<String, String> firstParam,
                                                        MultiValueMap<String, String> secondParam) throws Exception {
        return mockMvc.perform(get(url)
                        .params(firstParam)
                        .params(secondParam))
                .andDo(print());
    }

    public static ResultActions getRequestWithHeadersAndParams(MockMvc mockMvc, String url,
                                                               HttpHeaders headers,
                                                               MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform(get(url)
                        .params(params)
                        .headers(headers))
                .andDo(print());
    }
}
