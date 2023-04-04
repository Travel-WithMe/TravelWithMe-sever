package com.frog.travelwithme.global.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * MultiResponseDto 설명: 다중 공통 응답 Dto
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/02
 **/
@Getter
public class MultiResponseDto<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public MultiResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
