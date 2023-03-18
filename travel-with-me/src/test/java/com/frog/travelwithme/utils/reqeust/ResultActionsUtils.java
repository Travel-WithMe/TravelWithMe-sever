package com.frog.travelwithme.utils.reqeust;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ResultActionsUtils {

    public static ResultActions postRequest(MockMvc mockMvc,
                                            String url,
                                            HttpHeaders headers) throws Exception {
        return mockMvc.perform(post(url)
                .headers(headers))
//                .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequest(MockMvc mockMvc,
                                            String url,
                                            String json) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
//                .with(csrf()))
                .andDo(print());
    }

    public static ResultActions postRequest(MockMvc mockMvc,
                                            String url,
                                            String json,
                                            HttpHeaders headers) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .headers(headers))
//                .with(csrf()))
                .andDo(print());
    }

    public static ResultActions patchRequest(MockMvc mockMvc,
                                             String url,
                                             String json,
                                             HttpHeaders headers) throws Exception {
        return mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .headers(headers))
//                .with(csrf()))
                .andDo(print());
    }

    public static ResultActions deleteRequest(MockMvc mockMvc,
                                              String url,
                                              HttpHeaders headers) throws Exception {
        return mockMvc.perform(delete(url)
                .headers(headers))
//                .with(csrf()))
                .andDo(print());

    }

    public static ResultActions getRequest(MockMvc mockMvc,
                                           String url,
                                           HttpHeaders headers) throws Exception {
        return mockMvc.perform(get(url)
                .headers(headers))
//                .with(csrf()))
                .andDo(print());

    }

    public static ResultActions getRequest(MockMvc mockMvc, String url,
                                           HttpHeaders headers,
                                           MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform(get(url)
                .params(params)
                .headers(headers))
                .andDo(print());
    }
}
