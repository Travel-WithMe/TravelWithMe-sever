package com.frog.travelwithme.utils.response;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ResponsePostSnippet {
    public static ResponseFieldsSnippet getPostPageSnippet() {
        return ResponsePageSnippet.getPageSnippet()
                .and(
                        fieldWithPath("data[].id").description("게시글 ID"),
                        fieldWithPath("data[].user").description("작성자 닉네임 및 타입"),
                        fieldWithPath("data[].title").description("제목"),
                        fieldWithPath("data[].content").description("내용"),
                        fieldWithPath("data[].totalLikeCount").description("좋아요 수"),
                        fieldWithPath("data[].likeStatus").description("좋아요 여부")
                );
    }
}
