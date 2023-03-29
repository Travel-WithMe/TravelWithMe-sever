package com.frog.travelwithme.global.exception;


import lombok.Getter;

/**
 * BusinessLogicException 설명: ExceptionCode를 받아 예외 정보를 제공
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
public class BusinessLogicException extends RuntimeException {

    @Getter
    private final ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
