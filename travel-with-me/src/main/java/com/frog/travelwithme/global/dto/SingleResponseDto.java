package com.frog.travelwithme.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SingleResponseDto 설명: 단일 공통 응답 Dto
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/02
 **/
@AllArgsConstructor
@Getter
public class SingleResponseDto<T> {
    private T data;
}
