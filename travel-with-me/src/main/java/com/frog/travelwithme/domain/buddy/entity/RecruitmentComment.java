package com.frog.travelwithme.domain.buddy.entity;

import com.frog.travelwithme.domain.common.comment.entity.Comment;
import com.frog.travelwithme.domain.common.DeletionEntity;
import com.frog.travelwithme.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentComment extends Comment {

    @Embedded
    private DeletionEntity deletionEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToMany
    @JoinTable(name = "recruitment_comment_like",
            joinColumns = @JoinColumn(name = "recruitment_comment_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> likedMembers = new ArrayList<>();

    @Builder
    public RecruitmentComment(Long id, Integer depth, Long groupId, Long taggedMemberId,
                              String content, DeletionEntity deletionEntity) {
        super(id, depth, groupId, taggedMemberId, content);
        this.deletionEntity = deletionEntity;
    }

    public RecruitmentComment addMember(Member member) {
        if (member == null) {
            return null;
        }
        this.member = member;
        return this;
    }

    public RecruitmentComment addRecruitment(Recruitment recruitment) {
        if (recruitment == null) {
            return null;
        }
        this.recruitment = recruitment;
        return this;
    }

    public void updateDeletionEntity() {
        this.deletionEntity.setIsDeleted(true);
        this.deletionEntity.setDeletedAt(LocalDateTime.now());
    }

}
