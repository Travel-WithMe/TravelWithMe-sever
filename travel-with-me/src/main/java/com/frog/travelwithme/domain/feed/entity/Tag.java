package com.frog.travelwithme.domain.feed.entity;

import lombok.*;

import javax.persistence.*;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/30
 **/
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String name;

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
