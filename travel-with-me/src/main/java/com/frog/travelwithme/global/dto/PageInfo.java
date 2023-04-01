package com.frog.travelwithme.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageInfo {
    private int page;
    private int size;
    private Long totalElements;
    private int totalPages;
}
