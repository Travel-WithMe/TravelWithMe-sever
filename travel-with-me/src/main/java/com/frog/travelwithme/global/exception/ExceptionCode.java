package com.frog.travelwithme.global.exception;

import lombok.Getter;

/**
 * ErrorCode 설명: 예외 코드 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
public enum ExceptionCode {

    // Member
    MEMBER_ROLE_DOES_NOT_EXISTS(404, "회원의 권한이 존재하지 않습니다."),
    MEMBER_EXISTS(404, "이미 존재하는 회원입니다."),
    AUTH_CODE_IS_NOT_SAME(404, "인증 번호가 일치하지 않습니다."),
    UNABLE_TO_SEND_EMAIL(404, "메일을 전송할 수 없습니다."),
    NO_SUCH_ALGORITHM(400, "인증 번호 생성을 위한 알고리즘을 찾을 수 없습니다."),

    // Security, JWT
    NO_ACCESS_TOKEN(403, "토큰에 권한 정보가 존재하지 않습니다."),
    TOKEN_SIGNATURE_INVALID(400, "토큰 Signature가 올바르지 않습니다."),
    TOKEN_EXPIRED(400, "토큰이 만료되었습니다."),
    TOKEN_UNSUPPORTED(400, "지원하지 않는 형식의 토큰입니다."),
    TOKEN_ILLEGAL_ARGUMENT(400, "올바르지 않은 토큰입니다."),
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
    HEADER_REFRESH_TOKEN_NOT_EXISTS(404, "헤더에 Refresh token이 존재하지 않습니다."),
    TOKEN_IS_NOT_SAME(404, "토큰이 일치하지 않습니다."),

    // AES
    ENCRYPTION_FAILED(404, "암호화에 실패했습니다."),
    DECRYPTION_FAILED(404, "복호화에 실패했습니다."),
    SECRET_KEY_INVALID(400, "Secret key가 올바르지 않습니다."),
    MEMBER_ROLE_INVALID(400, "회원의 권한 정보가 올바르지 않습니다."),

    // Redis
    NOT_FOUND_AVAILABLE_PORT(404, "사용 가능한 Port를 찾을 수 없습니다. port: 10000 ~ 65535"),
    ERROR_EXECUTING_EMBEDDED_REDIS(404, "EmbeddedRedis 실행 중 에러 발생"),
    REDIS_SERVER_EXCUTABLE_NOT_FOUND(404, "Redis Server Executable not found");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
