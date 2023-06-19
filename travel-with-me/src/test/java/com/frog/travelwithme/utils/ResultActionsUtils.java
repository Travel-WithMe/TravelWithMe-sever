package com.frog.travelwithme.utils;

import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public static ResultActions postRequestWithContentAndCsrf(MockMvc mockMvc,
                                                              String url,
                                                              String json) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequestWithToken(MockMvc mockMvc,
                                                     String url,
                                                     String accessToken,
                                                     String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions postRequestWithUserDetails(MockMvc mockMvc,
                                                           String url,
                                                           CustomUserDetails userDetails) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user(userDetails)))
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

    public static ResultActions postRequestWithParamsWithCsrf(MockMvc mockMvc,
                                                              String url,
                                                              MultiValueMap<String, String> paprams) throws Exception {
        return mockMvc.perform(post(url)
                        .params(paprams)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequestWithParams(MockMvc mockMvc,
                                                      String url,
                                                      MultiValueMap<String, String> paprams) throws Exception {
        return mockMvc.perform(post(url)
                        .params(paprams))
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

    public static ResultActions postRequestWithContent(MockMvc mockMvc,
                                                       String url,
                                                       String json) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json))
                .andDo(print());
    }

    public static ResultActions postRequestWithUserDetailsAndTwoMultiPart(MockMvc mockMvc,
                                                                          String url,
                                                                          CustomUserDetails userDetails,
                                                                          List<MockMultipartFile> files,
                                                                          MockMultipartFile data) throws Exception {
        MockMultipartHttpServletRequestBuilder requestBuilder = (MockMultipartHttpServletRequestBuilder) multipart(url)
                .file(data)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(user(userDetails))
                .with(csrf());
        for (MockMultipartFile file : files) {
            requestBuilder.file(file);
        }
        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    public static ResultActions postRequestWithTokenAndMultiPart(MockMvc mockMvc,
                                                                 String url,
                                                                 String accessToken,
                                                                 String encryptedRefreshToken,
                                                                 MockMultipartFile file,
                                                                 MockMultipartFile data) throws Exception {
        return mockMvc.perform(multipart(url)
                        .file(file)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions postRequestWithTokenAndPathvariable(MockMvc mockMvc,
                                                                    String url,
                                                                    Object pathVariable,
                                                                    String accessToken,
                                                                    String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.post(url, pathVariable)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions postRequestWithTokenAndPathVariable(MockMvc mockMvc,
                                                                    String url,
                                                                    Object pathVariable,
                                                                    String accessToken,
                                                                    String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.post(url, pathVariable)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions postRequestWithTokenAndPathVariableAndContent(MockMvc mockMvc,
                                                                              String url,
                                                                              Object pathVariable,
                                                                              String json,
                                                                              String accessToken,
                                                                              String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.post(url, pathVariable)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequestWithCsrf(MockMvc mockMvc,
                                                     String url) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print());
    }

    public static ResultActions patchRequest(MockMvc mockMvc,
                                             String url) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    public static ResultActions patchRequestWithToken(MockMvc mockMvc,
                                                      String url,
                                                      String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
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
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequestWithMultiPartAndToken(MockMvc mockMvc,
                                                                  String url,
                                                                  MockMultipartFile file,
                                                                  String accessToken,
                                                                  String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(multipart(HttpMethod.PATCH, url)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions patchRequestWithUserDetailsAndMultiPart(MockMvc mockMvc,
                                                                        String url,
                                                                        CustomUserDetails userDetails,
                                                                        MockMultipartFile file) throws Exception {
        return mockMvc.perform(multipart(HttpMethod.PATCH, url)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(user(userDetails))
                        .with(csrf()))
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

    public static ResultActions patchRequestWithTwoMultipartAndUserDetails(MockMvc mockMvc,
                                                                           String url,
                                                                           MockMultipartFile file,
                                                                           MockMultipartFile data,
                                                                           CustomUserDetails userDetails) throws Exception {
        return mockMvc.perform(multipart(HttpMethod.PATCH, url)
                        .file(file)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                        .with(user(userDetails)))
                .andDo(print());
    }

    public static ResultActions patchRequestWithTwoMultiPartAndToken(MockMvc mockMvc,
                                                                     String url,
                                                                     String accessToken,
                                                                     String encryptedRefreshToken,
                                                                     MockMultipartFile file,
                                                                     MockMultipartFile data) throws Exception {
        return mockMvc.perform(multipart(HttpMethod.PATCH, url)
                        .file(file)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions postRequestWithTokenAndMultipartListAndMultipartData(MockMvc mockMvc,
                                                                                     String url,
                                                                                     String accessToken,
                                                                                     String encryptedRefreshToken,
                                                                                     List<MockMultipartFile> files,
                                                                                     MockMultipartFile data) throws Exception {
        MockMultipartHttpServletRequestBuilder requestBuilder = (MockMultipartHttpServletRequestBuilder) multipart(url)
                .file(data)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                .header(REFRESH_HEADER, encryptedRefreshToken);
        for (MockMultipartFile file : files) {
            requestBuilder.file(file);
        }
        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    public static ResultActions patchRequestWithTwoMultiPartAndToken(MockMvc mockMvc,
                                                                     String url,
                                                                     String accessToken,
                                                                     String encryptedRefreshToken,
                                                                     List<MockMultipartFile> files,
                                                                     MockMultipartFile data) throws Exception {
        MockMultipartHttpServletRequestBuilder requestBuilder = (MockMultipartHttpServletRequestBuilder) multipart(HttpMethod.PATCH, url)
                .file(data)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                .header(REFRESH_HEADER, encryptedRefreshToken);
        for (MockMultipartFile file : files) {
            requestBuilder.file(file);
        }
        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    public static ResultActions patchRequestWithTokenAndPathVariableAndContent(MockMvc mockMvc,
                                                                               String url,
                                                                               Object pathVariable,
                                                                               String json,
                                                                               String accessToken,
                                                                               String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.patch(url, pathVariable)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
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
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions deleteRequestWithTokenAndPathVariable(MockMvc mockMvc,
                                                                      String url,
                                                                      Object pathVariable,
                                                                      String accessToken,
                                                                      String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.delete(url, pathVariable)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions getRequest(MockMvc mockMvc,
                                           String url) throws Exception {
        return mockMvc.perform(get(url)
                        .with(csrf()))
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
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
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

    public static ResultActions getRequestWithTokenAndTwoParams(MockMvc mockMvc,
                                                                String url,
                                                                String accessToken,
                                                                String encryptedRefreshToken,
                                                                MultiValueMap<String, String> tagNamePapram,
                                                                MultiValueMap<String, String> sizePapram) throws Exception {
        return mockMvc.perform(get(url)
                        .params(tagNamePapram)
                        .params(sizePapram)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions getRequestWithTokenAndPathVariable(MockMvc mockMvc,
                                                                   String url,
                                                                   Object pathVaraible,
                                                                   String accessToken,
                                                                   String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.get(url, pathVaraible)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions getRequestWithTwoParamsAndToken(MockMvc mockMvc,
                                                                String url,
                                                                MultiValueMap<String, String> lastFeedIdParam,
                                                                MultiValueMap<String, String> nicknameParam,
                                                                String accessToken,
                                                                String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(lastFeedIdParam)
                        .params(nicknameParam)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }

    public static ResultActions getRequestWithTokenAndParam(MockMvc mockMvc,
                                                            String url,
                                                            MultiValueMap<String, String> param,
                                                            String accessToken,
                                                            String encryptedRefreshToken) throws Exception {
        return mockMvc.perform(get(url)
                        .params(param)
                        .header(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken)
                        .header(REFRESH_HEADER, encryptedRefreshToken))
                .andDo(print());
    }
}
