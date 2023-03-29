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
    TOKEN_SIGNATURE_INVALID(400, "Token Signature Invalid"),
    TOKEN_EXPIRED(400, "Token Expired"),
    TOKEN_UNSUPPORTED(400, "Token Unsupported"),
    TOKEN_ILLEGAL_ARGUMENT(400, "Token Illegal Argumnet"),

    // AES
    ENCRYPTION_FAILED(404, "암호화에 실패하였습니다."),
    DECRYPTION_FAILED(404, "복호화에 실패하였습니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
