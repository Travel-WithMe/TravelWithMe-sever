package com.frog.travelwithme.utils.snippet.response;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class ErrorResponseSnippet {
    public static ResponseFieldsSnippet getFieldErrorSnippet() {
        return getFieldErrorSnippets()
                .and(
                        fieldWithPath("fieldErrors[].field").description("필드"),
                        fieldWithPath("fieldErrors[].rejectedValue").description("거부된 값"),
                        fieldWithPath("fieldErrors[].reason").description("거부된 이유")
                );
    }

    public static ResponseFieldsSnippet getFieldErrorSnippets() {
        return responseFields(
                fieldWithPath("status").description("상태코드"),
                fieldWithPath("message").description("메시지"),
                fieldWithPath("fieldErrors").description("필드에러"),
                fieldWithPath("violationErrors").description("반복에러")
        );
    }
}
