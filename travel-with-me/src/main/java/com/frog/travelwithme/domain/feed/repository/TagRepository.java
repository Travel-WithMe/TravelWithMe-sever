package com.frog.travelwithme.domain.feed.repository;

import com.frog.travelwithme.domain.feed.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/03
 **/
public interface TagRepository extends JpaRepository<Tag, Long>, TagCustomRepository {
    Optional<Tag> findByName(String tagName);
}
