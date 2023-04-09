package com.frog.travelwithme.utils.response;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class ResponsePageSnippet {
    public static ResponseFieldsSnippet getPageSnippet() {
        return responseFields(
                fieldWithPath("pageInfo.page").description("현재 페이지"),
                fieldWithPath("pageInfo.size").description("페이지 사이즈"),
                fieldWithPath("pageInfo.totalElements").description("전체 페이지"),
                fieldWithPath("pageInfo.totalPages").description("전체 요소")
        );
    }
}
