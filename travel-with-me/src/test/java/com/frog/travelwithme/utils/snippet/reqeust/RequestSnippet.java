package com.frog.travelwithme.utils.snippet.reqeust;

import com.google.common.net.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
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
                        fieldWithPath("nation").type(STRING).description(
                                "회원 국가: CH / FR / IT / JP / KO / SP / TH / TU / UK / US"),
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

    public static Snippet getRecruitmentPathVariableSnippet() {
        return pathParameters(
                List.of(
                        parameterWithName("recruitment-id").description("접근하려는 동행 모집글 인덱스")
                ));
    }

    public static Snippet getCommentPathVariableSnippet() {
        return pathParameters(
                List.of(
                        parameterWithName("comment-id").description("접근하려는 댓글 인덱스")
                ));
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

    public static Snippet getSignUpSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("email").type(STRING).description("회원 이메일"),
                        fieldWithPath("password").type(STRING).description("회원 비밀번호"),
                        fieldWithPath("nickname").type(STRING).description("회원 닉네임"),
                        fieldWithPath("gender").type(STRING).description("회원 성별 (남자/여자)"),
                        fieldWithPath("address").type(STRING).description("회원 주소"),
                        fieldWithPath("nation").type(STRING).description(
                                "회원 국가: CH / FR / IT / JP / KO / SP / TH / TU / UK / US"),
                        fieldWithPath("role").type(STRING).description("회원 역할"),
                        fieldWithPath("interests").type(JsonFieldType.ARRAY).description(
                                "회원 관심사 : 하이킹 / 서핑 / 다이빙 / 스노클링 / 사파리 / 스키 / 자전거 / 액티비티 / " +
                                        "음식 체험 / 음악 감상 / 공연 감상 / 전시회 / 예술 관람 / 사진 촬영 / 지역 축제 / 계획형 / 즉흥형")
                )
        );
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

    public static Snippet getPostCommentSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("depth").type(NUMBER).description("댓글:1, 대댓글:2"),
                        fieldWithPath("groupId").type(NUMBER).description("작성된 댓글,대댓글의 Group ID").optional(),
                        fieldWithPath("content").type(STRING).description("댓글,대댓글 내용"),
                        fieldWithPath("taggedMemberId").type(NUMBER).description("언급(태그)된 회원 아이디").optional()
                )
        );
    }

    public static Snippet getPatchCommentSnippet() {
        return requestFields(
                List.of(
                        fieldWithPath("content").type(STRING).description("댓글,대댓글 내용"),
                        fieldWithPath("taggedMemberId").type(NUMBER).description("언급(태그)된 회원 아이디").optional()
                )
        );
    }

    public static Snippet getFollowingPathVariableSnippet() {
        return pathParameters(
                List.of(
                        parameterWithName("followee-email").description("팔로잉할 회원의 이메일")
                ));
    }

    public static Snippet getNicknamePathVariableSnippet() {
        return pathParameters(
                List.of(
                        parameterWithName("nickname").description("조회할 회원의 닉네임")
                ));
    }

    public static Snippet getCheckDuplicatedEmailParamSnippet() {
        return requestParameters(
                parameterWithName("email").description("중복 체크하려는 이메일")
        );
    }

    public static Snippet getCheckDuplicatedNicknameParamSnippet() {
        return requestParameters(
                parameterWithName("nickname").description("중복 체크하려는 닉네임")
        );
    }

    public static Snippet getFeedsByNicknameParamSnippet() {
        return requestParameters(
                parameterWithName("lastFeedId").description("이전 조회한 목록 중 마지막 Feed의 인덱스. 첫 번째 조회에서는 해당 파라미터 제외").optional(),
                parameterWithName("nickname").description("Feed 검색을 위한 nickname")
        );
    }

    public static Snippet getFeedsByTagParamSnippet() {
        return requestParameters(
                parameterWithName("lastFeedId").description("이전 조회한 목록 중 마지막 Feed의 인덱스. 첫 번째 조회에서는 해당 파라미터 제외").optional(),
                parameterWithName("tag").description("Feed 검색을 위한 tag")
        );
    }

    public static Snippet getAllFeedParamSnippet() {
        return requestParameters(
                parameterWithName("lastFeedId").description("이전 조회한 목록 중 마지막 Feed의 인덱스. 첫 번째 조회에서는 해당 파라미터 제외").optional()
        );
    }
}
