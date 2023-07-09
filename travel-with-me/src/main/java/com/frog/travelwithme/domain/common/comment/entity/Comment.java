package com.frog.travelwithme.domain.common.comment.entity;

import com.frog.travelwithme.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/24
 **/

@Getter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer depth;

    private Long groupId;

    private Long taggedMemberId;

    private String content;

    private Long likeCount = 0L;

    private Long commentCount = 0L;

    private boolean isDeleted = false;

    public Comment(Long id, Integer depth, Long groupId, Long taggedMemberId, String content){
        this.id = id;
        this.depth = depth;
        this.groupId = groupId;
        this.taggedMemberId = taggedMemberId;
        this.content = content;
    }

    public Comment addGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeTaggedMemberId(Long id) {
        this.taggedMemberId = id;
    }

    public boolean hasTaggedMember() {
        return this.taggedMemberId != null;
    }

    public void softDeleteComment() {
        this.isDeleted = true;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }
}
