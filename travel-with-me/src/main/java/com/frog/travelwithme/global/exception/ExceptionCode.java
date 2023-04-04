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
    MEMBER_ROLE_DOES_NOT_EXISTS(404, "Member role does not exists"),
    MEMBER_EXISTS(404, "Member exists"),

    // Security, JWT
    NO_ACCESS_TOKEN(403, "Token without permission information"),
    TOKEN_SIGNATURE_INVALID(400, "Token signature invalid"),
    TOKEN_EXPIRED(400, "Token expired"),
    TOKEN_UNSUPPORTED(400, "Token unsupported"),
    TOKEN_ILLEGAL_ARGUMENT(400, "Token illegal argumnet"),
    MEMBER_NOT_FOUND(404, "Member not found"),
    HEADER_REFRESH_TOKEN_NOT_EXISTS(404, "No refresh token in header"),
    TOKEN_IS_NOT_SAME(404, "Token is not same"),

    // AES
    ENCRYPTION_FAILED(404, "Encryption failed"),
    DECRYPTION_FAILED(404, "Decryption failed.\n"),
    SECRET_KEY_INVALID(400, "Secret key invalid"),
    MEMBER_ROLE_INVALID(400, "Member role invalid");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
