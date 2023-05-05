package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.controller.dto.TagDto;

import java.util.List;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/03
 **/
public interface TagCustomRepository {

    List<TagDto.Response> findTagsStartingWith(String tagName, int size);
}
