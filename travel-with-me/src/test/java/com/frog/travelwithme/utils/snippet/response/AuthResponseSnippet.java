package com.frog.travelwithme.utils.snippet.response;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class AuthResponseSnippet {
    public static ResponseFieldsSnippet getLonginSuccessResponseSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 닉네임 및 타입"),
                        fieldWithPath("data.role").type(JsonFieldType.STRING).description("회원 역할")
                )
        );
    }
}
