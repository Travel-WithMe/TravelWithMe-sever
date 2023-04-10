package com.frog.travelwithme.utils.snippet.reqeust;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

public class AuthRequestSnippet {
    public static RequestFieldsSnippet getLoginSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호")
                )
        );
    }
}
