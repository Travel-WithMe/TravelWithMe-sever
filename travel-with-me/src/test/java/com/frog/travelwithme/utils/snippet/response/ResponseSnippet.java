package com.frog.travelwithme.utils.snippet.response;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;

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
                        fieldWithPath("data.lastModifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정일"),
                        fieldWithPath("data.isFollow").type(JsonFieldType.BOOLEAN).description("해당 회원 팔로우 여부"),
                        fieldWithPath("data.followerCount").type(JsonFieldType.NUMBER).description("회원의 팔로워 수"),
                        fieldWithPath("data.followingCount").type(JsonFieldType.NUMBER).description("회원의 팔로잉 수")
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

    public static Snippet getFeedSnippet() {
        return responseFields(
                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("피드 인덱스"),
                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("작성자 프로필 이미지 URL"),
                fieldWithPath("data.contents").type(JsonFieldType.STRING).description("피드 내용"),
                fieldWithPath("data.location").type(JsonFieldType.STRING).description("피드를 작성한 위치"),
                fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요 개수"),
                fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER).description("피드 댓글 개수"),
                fieldWithPath("data.isWriter").type(JsonFieldType.BOOLEAN).description("사용자의 피드 작성자 여부"),
                fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("사용자의 피드 좋아요 여부"),
                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("피드 생성 시간"),
                fieldWithPath("data.tags").type(JsonFieldType.ARRAY).description("피드 태그 리스트"),
                fieldWithPath("data.imageUrls").type(JsonFieldType.ARRAY).description("피드 이미지 URL 리스트")
        );
    }

    public static ResponseFieldsSnippet getFeedsSnippet() {
        return responseFields(
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("피드 인덱스"),
                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                        fieldWithPath("data[].profileImage").type(JsonFieldType.STRING).description("작성자 프로필 이미지 URL"),
                        fieldWithPath("data[].contents").type(JsonFieldType.STRING).description("피드 내용"),
                        fieldWithPath("data[].location").type(JsonFieldType.STRING).description("피드를 작성한 위치"),
                        fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요 개수"),
                        fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("피드 댓글 개수"),
                        fieldWithPath("data[].isWriter").type(JsonFieldType.BOOLEAN).description("사용자의 피드 작성자 여부"),
                        fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("사용자의 피드 좋아요 여부"),
                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("피드 생성 시간"),
                        fieldWithPath("data[].tags").type(JsonFieldType.ARRAY).description("피드 태그 리스트"),
                        fieldWithPath("data[].imageUrls").type(JsonFieldType.ARRAY).description("피드 이미지 URL 리스트")
                );
    }

    public static Snippet getTagsSnippet() {
        return responseFields(
                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("태그 이름"),
                fieldWithPath("data[].count").type(JsonFieldType.NUMBER).description("태그가 사용된 횟수")
        );
    }

    public static Snippet getErrorSnippet() {
        return responseFields(
                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 Status"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지"),
                fieldWithPath("fieldErrors").description("Field 에러"),
                fieldWithPath("violationErrors").description("객체 에러")
        );
    }

    public static Snippet getPostCommentSnippet() {
        return responseFields(
                fieldWithPath("data.commentId").type(JsonFieldType.NUMBER).description("작성된 댓글,대댓글 ID"),
                fieldWithPath("data.depth").type(JsonFieldType.NUMBER).description("작성된 댓글,대댓글 여부 (댓글:1, 대댓글:2)"),
                fieldWithPath("data.content").type(JsonFieldType.STRING).description("작성된 댓글,대댓글 내용"),
                fieldWithPath("data.taggedMemberId").type(JsonFieldType.NULL).description("작성된 댓글,대댓글의 언급(태그)된 회원 ID")
        );
    }

    public static Snippet getPostCommentWithTaggedSnippet() {
        return responseFields(
                fieldWithPath("data.commentId").type(JsonFieldType.NUMBER).description("작성된 댓글,대댓글 ID"),
                fieldWithPath("data.depth").type(JsonFieldType.NUMBER).description("작성된 댓글,대댓글 여부 (댓글:1, 대댓글:2)"),
                fieldWithPath("data.content").type(JsonFieldType.STRING).description("작성된 댓글,대댓글 내용"),
                fieldWithPath("data.taggedMemberId").type(JsonFieldType.NUMBER).description("작성된 댓글,대댓글의 언급(태그)된 회원 ID")
        );
    }

}
