package com.frog.travelwithme.utils.snippet.reqeust;

import com.google.common.net.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

/**
 * RequestPostSnippet 설명: requestFields 관리
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/18
 **/
public class RequestSnippet {

    public static Snippet getMemberPatchSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("password").type(STRING).description("회원 비밀번호"),
                        fieldWithPath("nickname").type(STRING).description("회원 닉네임"),
                        fieldWithPath("address").type(STRING).description("회원 주소"),
                        fieldWithPath("introduction").type(STRING).description("자기 소개"),
                        fieldWithPath("gender").type(STRING).description("회원 성별 (남자/여자)"),
                        fieldWithPath("nation").type(STRING).description("회원 국가"),
                        fieldWithPath("interests").type(JsonFieldType.ARRAY).description(
                                "회원 관심사 : 하이킹 / 서핑 / 다이빙 / 스노클링 / 사파리 / 스키 / 자전거 / 액티비티 / " +
                                        "음식 체험 / 음악 감상 / 공연 감상 / 전시회 / 예술 관람 / 사진 촬영 / 지역 축제 / 계획형 / 즉흥형")));
    }

    public static RequestFieldsSnippet getPostRecruitmentSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("title").type(STRING).description("동행모집 게시글 제목"),
                        fieldWithPath("content").type(STRING).description("동행모집 게시글 내용"),
                        fieldWithPath("travelNationality").type(STRING).description("동행모집 국가"),
                        fieldWithPath("travelStartDate").type(STRING).description("동행모집 여행 시작날짜"),
                        fieldWithPath("travelEndDate").type(STRING).description("동행모집 여행 종료날짜")
                )
        );
    }

    public static RequestFieldsSnippet getPatchRecruitmentSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("title").type(STRING).description("동행모집 게시글 제목"),
                        fieldWithPath("content").type(STRING).description("동행모집 게시글 내용"),
                        fieldWithPath("travelNationality").type(STRING).description("동행모집 국가"),
                        fieldWithPath("travelStartDate").type(STRING).description("동행모집 여행 시작날짜"),
                        fieldWithPath("travelEndDate").type(STRING).description("동행모집 여행 종료날짜")
                )
        );
    }

    public static Snippet getMailVerificiationRequestSnippet() {
        return requestParameters(
                List.of(
                        parameterWithName("email").description("인증 번호를 전달 받은 이메일 주소")
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

    public static Snippet getProfileImageMultipartSnippet() {
        return requestParts(
                partWithName("file").description("회원의 프로필 이미지").optional());
    }

    public static Snippet getSignUpMultipartSnippet() {
        return requestParts(
                partWithName("file").description("회원의 프로필 이미지").optional(),
                partWithName("data").description("회원 데이터"));
    }

    public static Snippet getSignUpMultipartDataFieldSnippet() {
        return requestPartFields("data",
                fieldWithPath("email").type(STRING).description("회원 이메일"),
                fieldWithPath("password").type(STRING).description("회원 비밀번호"),
                fieldWithPath("nickname").type(STRING).description("회원 닉네임"),
                fieldWithPath("gender").type(STRING).description("회원 성별 (남자/여자)"),
                fieldWithPath("address").type(STRING).description("회원 주소"),
                fieldWithPath("introduction").type(STRING).description("자기 소개"),
                fieldWithPath("nation").type(STRING).description("회원 국가"),
                fieldWithPath("role").type(STRING).description("회원 역할"),
                fieldWithPath("interests").type(JsonFieldType.ARRAY).description(
                        "회원 관심사 : 하이킹 / 서핑 / 다이빙 / 스노클링 / 사파리 / 스키 / 자전거 / 액티비티 / " +
                                "음식 체험 / 음악 감상 / 공연 감상 / 전시회 / 예술 관람 / 사진 촬영 / 지역 축제 / 계획형 / 즉흥형"));
    }

    public static Snippet getTokenSnippet() {
        return requestHeaders(
                List.of(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token, 만료 시간: 2시간"),
                        headerWithName(HttpHeaders.REFRESH).description("암호화된 Refresh Token, 만료 시간: 2주")
                )
        );
    }

    public static Snippet getRefreshTokenSnippet() {
        return requestHeaders(
                List.of(
                        headerWithName(HttpHeaders.REFRESH).description("암호화된 Refresh Token, 만료 시간: 2주")
                )
        );
    }

    public static Snippet getPostFeedMultipartSnippet() {
        return requestParts(
                partWithName("files").description("피드 이미지"),
                partWithName("data").description("피드 작성 정보")
        );
    }


    public static Snippet getPostFeedMultipartDataFieldSnippet() {
        return requestPartFields("data",
                fieldWithPath("contents").type(STRING).description("동행모집 게시글 내용"),
                fieldWithPath("location").type(JsonFieldType.STRING).description("피드를 작성한 위치"),
                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("피드 태그 리스트"));
    }

    public static Snippet getPatchFeedMultipartDataFieldSnippet() {
        return requestPartFields("data",
                fieldWithPath("contents").type(STRING).description("동행모집 게시글 내용"),
                fieldWithPath("location").type(JsonFieldType.STRING).description("피드를 작성한 위치"),
                fieldWithPath("tags").type(JsonFieldType.ARRAY).description("피드 태그 리스트"),
                fieldWithPath("removeImageUrls").type(JsonFieldType.ARRAY).description("삭제하려는 피드 이미지 URL"));
    }

    public static Snippet getFeedPathVariableSnippet() {
        return pathParameters(
                List.of(
                        parameterWithName("feed-id").description("접근하려는 피드 인덱스")
                ));
    }

    public static Snippet getTagParamSnippet() {
        return requestParameters(
                parameterWithName("tagName").description("조회하려는 태그 이름"),
                parameterWithName("size").description("조회하려는 태그 개수").optional()
        );
    }
}
