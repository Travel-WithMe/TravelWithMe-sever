package com.frog.travelwithme.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * PagelessMultiResponseDto 설명: 페이지 정보를 제외한 다중 응답 Dto
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/02
 **/
@Getter
@AllArgsConstructor
public class PagelessMultiResponseDto<T> {
    private List<T> data;
}
