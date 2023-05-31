package com.frog.travelwithme.domain.member.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Member 설명: 회원 데이터 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/29
 **/
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToMany(mappedBy = "interests")
    private List<Member> members;
}
