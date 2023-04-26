package com.frog.travelwithme.utils.snippet.reqeust;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

/**
 * RequestPostSnippet 설명: requestFields 관리
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/18
 **/
public class RequestSnippet {
    public static RequestFieldsSnippet getSignUpSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                        fieldWithPath("gender").type(JsonFieldType.STRING).description("회원 성별"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("회원 주소"),
                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기 소개"),
                        fieldWithPath("nation").type(JsonFieldType.STRING).description("회원 국가"),
                        fieldWithPath("role").type(JsonFieldType.STRING).description("회원 역할")
                )
        );
    }

    public static Snippet getMemberPatchSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("회원 주소"),
                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기 소개"),
                        fieldWithPath("gender").type(JsonFieldType.STRING).description("회원 성별"),
                        fieldWithPath("nation").type(JsonFieldType.STRING).description("회원 국가")
                )
        );
    }

    public static RequestFieldsSnippet getPostRecruitmentSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("동행모집 게시글 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("동행모집 게시글 내용"),
                        fieldWithPath("travelNationality").type(JsonFieldType.STRING).description("동행모집 국가"),
                        fieldWithPath("travelStartDate").type(JsonFieldType.STRING).description("동행모집 여행 시작날짜"),
                        fieldWithPath("travelEndDate").type(JsonFieldType.STRING).description("동행모집 여행 종료날짜")
                )
        );
    }

    public static RequestFieldsSnippet getPatchRecruitmentSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("동행모집 게시글 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("동행모집 게시글 내용"),
                        fieldWithPath("travelNationality").type(JsonFieldType.STRING).description("동행모집 국가"),
                        fieldWithPath("travelStartDate").type(JsonFieldType.STRING).description("동행모집 여행 시작날짜"),
                        fieldWithPath("travelEndDate").type(JsonFieldType.STRING).description("동행모집 여행 종료날짜")
                )
        );
    }

    public static Snippet getMailVerificiationRequestSnippet() {
        return requestParameters(
                List.of(
                        parameterWithName("email").description("인증 번호를 전달 받은 이메일 주소"),
                        parameterWithName("_csrf").ignored()
                )
        );
    }

    public static Snippet getMailVerificiationSnippet() {
        return requestParameters(
                List.of(
                        parameterWithName("email").description("인증 번호를 전달 받은 이메일 주소"),
                        parameterWithName("code").description("사용자가 인증 요청한 인증 번호")
                )
        );
    }

    public static Snippet getSignUpMultipartSnippet() {
        return requestParts(
                List.of(
                        partWithName("file").description("회원의 프로필 이미지").optional()
                )
        );
    }
}
