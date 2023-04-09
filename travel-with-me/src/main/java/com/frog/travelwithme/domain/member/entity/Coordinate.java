package com.frog.travelwithme.domain.member.entity;

import lombok.Data;

import javax.persistence.Embeddable;


/**
 * Coordinate 설명: 회원의 현재 위치 관리(위도/경도)
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/29
 **/
@Data
@Embeddable
public class Coordinate {
    private Double latitude;
    private Double longitude;
}
