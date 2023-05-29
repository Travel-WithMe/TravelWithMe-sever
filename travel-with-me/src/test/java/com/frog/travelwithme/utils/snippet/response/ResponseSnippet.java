package com.frog.travelwithme.utils.snippet.response;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class ResponseSnippet {
    public static ResponseFieldsSnippet getMemberSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 닉네임 및 타입"),
                        fieldWithPath("data.nation").type(JsonFieldType.STRING).description("회원 국가"),
                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("회원 주소"),
                        fieldWithPath("data.image").type(JsonFieldType.STRING).description("프로필 이미지 url"),
                        fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("자기소개"),
                        fieldWithPath("data.role").type(JsonFieldType.STRING).description("회원 역할"),
                        fieldWithPath("data.gender").type(JsonFieldType.STRING).description("회원 성별 (남자/여자)"),
                        fieldWithPath("data.interests").type(JsonFieldType.ARRAY).description("회원 관심사"),
                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 가입일"),
                        fieldWithPath("data.lastModifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정일")
                )
        );
    }

    public static ResponseFieldsSnippet getMemberPageSnippet() {
        return PageResponseSnippet.getPageSnippet()
                .and(
                        fieldWithPath("data[].id").description("회원 ID"),
                        fieldWithPath("data[].nickname").description("작성자 닉네임 및 타입"),
                        fieldWithPath("data[].nation").description("회원 국가"),
                        fieldWithPath("data[].address").description("회원 주소"),
                        fieldWithPath("data[].image").description("프로필 이미지 url"),
                        fieldWithPath("data[].introduction").description("자기소개"),
                        fieldWithPath("data[].role").description("회원 역할"),
                        fieldWithPath("data[].createdAt").description("회원 가입일")
                );
    }

    public static ResponseFieldsSnippet getPostRecruitmentSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("동행모집 게시글 아이디"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("동행모집 게시글 제목"),
                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("동행모집 게시글 내용"),
                        fieldWithPath("data.travelNationality").type(JsonFieldType.STRING).description("동행모집 국가"),
                        fieldWithPath("data.travelStartDate").type(JsonFieldType.STRING).description("동행모집 여행 시작날짜"),
                        fieldWithPath("data.travelEndDate").type(JsonFieldType.STRING).description("동행모집 여행 종료날짜"),
                        fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("동행모집 게시글 조회수"),
                        fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER).description("동행모집 게시글 댓글수"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 닉네임 및 타입"),
                        fieldWithPath("data.memberImage").type(JsonFieldType.STRING).description("프로필 이미지 url")
                )
        );
    }

    public static ResponseFieldsSnippet getMailVerificationSnippet() {
        return responseFields(
                fieldWithPath("data.success").type(JsonFieldType.BOOLEAN).description("이메일 인증 성공 여부")
        );
    }
    public static ResponseFieldsSnippet getPatchRecruitmentSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("동행모집 게시글 아이디"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("동행모집 게시글 제목"),
                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("동행모집 게시글 내용"),
                        fieldWithPath("data.travelNationality").type(JsonFieldType.STRING).description("동행모집 국가"),
                        fieldWithPath("data.travelStartDate").type(JsonFieldType.STRING).description("동행모집 여행 시작날짜"),
                        fieldWithPath("data.travelEndDate").type(JsonFieldType.STRING).description("동행모집 여행 종료날짜")
                )
        );
    }

    public static ResponseFieldsSnippet getMatchingMemberListSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("data[].id").description("회원 ID"),
                        fieldWithPath("data[].nickname").description("작성자 닉네임 및 타입"),
                        fieldWithPath("data[].image").description("프로필 이미지 url")
                )
        );
    }

    public static ResponseFieldsSnippet getMatchingSnippet() {
        return responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING).description("매칭 신청 성공 메세지")
        );
    }
}
