package com.frog.travelwithme.global.security.auth.utils;

import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.controller.dto.MemberDto;
import com.frog.travelwithme.global.dto.SingleResponseDto;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ErrorResponse;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Responder 설명: ErrorResponse를 클라이언트에게 전송
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
public class Responder {
    public static void sendErrorResponse(HttpServletResponse response, HttpStatus status) throws IOException {
        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }

    public static void sendErrorResponse(HttpServletResponse response, ExceptionCode code) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        throw new BusinessLogicException(code);
    }

    public static void loginSuccessResponse(HttpServletResponse response, Member member) throws IOException {
        Gson gson = new Gson();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        MemberDto.LoginResponse lgoinResponse = MemberDto.LoginResponse.builder()
                .id(member.getId())
                .eamil(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();

        response.getWriter().write(gson.toJson(new SingleResponseDto<>(lgoinResponse), SingleResponseDto.class));
    }
}
