package com.frog.travelwithme.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PageInfo 설명: 페이지 정보 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/02
 **/
@AllArgsConstructor
@Getter
public class PageInfo {
    private int page;
    private int size;
    private Long totalElements;
    private int totalPages;
}
