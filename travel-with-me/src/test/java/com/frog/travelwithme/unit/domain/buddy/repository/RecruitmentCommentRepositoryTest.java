package com.frog.travelwithme.unit.domain.buddy.repository;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.entity.RecruitmentComment;
import com.frog.travelwithme.domain.buddy.repository.MatchingRepository;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentCommentRepository;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.global.config.QuerydslConfig;
import com.frog.travelwithme.utils.StubData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.frog.travelwithme.global.enums.EnumCollection.MatchingStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/31
 **/

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
@ExtendWith(SpringExtension.class)
class RecruitmentCommentRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected RecruitmentCommentRepository recruitmentCommentRepository;

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 레포지토리 저장")
    void recruitmentCommentRepositoryTest1() {
        // given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();

        // when
        RecruitmentComment savedRecruitmentComment = recruitmentCommentRepository.save(recruitmentComment);

        entityManager.clear();
        entityManager.flush();

        RecruitmentComment findRecruitmentComment =
                recruitmentCommentRepository.findById(savedRecruitmentComment.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findRecruitmentComment.getDepth(), recruitmentComment.getDepth()),
                () -> assertEquals(findRecruitmentComment.getGroupId(), recruitmentComment.getGroupId()),
                () -> assertEquals(findRecruitmentComment.getTaggedMemberId(), recruitmentComment.getTaggedMemberId()),
                () -> assertEquals(findRecruitmentComment.getContent(), recruitmentComment.getContent())
        );
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 레포지토리 수정")
    void recruitmentCommentRepositoryTest2() {
        // given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        RecruitmentComment savedRecruitmentComment = recruitmentCommentRepository.save(recruitmentComment);

        entityManager.clear();
        entityManager.flush();

        RecruitmentComment findRecruitmentComment =
                recruitmentCommentRepository.findById(savedRecruitmentComment.getId()).get();

        // when
        findRecruitmentComment.changeTaggedMemberId(2L);

        // then
        assertThat(findRecruitmentComment.getId()).isEqualTo(savedRecruitmentComment.getId());
        assertThat(findRecruitmentComment.getTaggedMemberId()) // Expect : 2L
                .isNotEqualTo(recruitmentComment.getTaggedMemberId()); // Result : 1L
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 레포지토리 조회")
    void recruitmentCommentRepositoryTest3() {
        // given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        RecruitmentComment savedRecruitmentComment = recruitmentCommentRepository.save(recruitmentComment);

        entityManager.clear();
        entityManager.flush();

        // when
        RecruitmentComment findRecruitmentComment =
                recruitmentCommentRepository.findById(savedRecruitmentComment.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findRecruitmentComment.getDepth(), recruitmentComment.getDepth()),
                () -> assertEquals(findRecruitmentComment.getGroupId(), recruitmentComment.getGroupId()),
                () -> assertEquals(findRecruitmentComment.getTaggedMemberId(), recruitmentComment.getTaggedMemberId()),
                () -> assertEquals(findRecruitmentComment.getContent(), recruitmentComment.getContent())
        );
    }

    @Test
    @DisplayName("동행 모집글 댓글,대댓글 레포지토리 삭제")
    void recruitmentCommentRepositoryTest4() {
        // given
        RecruitmentComment recruitmentComment = StubData.MockComment.getRecruitmentComment();
        RecruitmentComment savedRecruitmentComment = recruitmentCommentRepository.save(recruitmentComment);

        // when
        recruitmentCommentRepository.delete(savedRecruitmentComment);

        Optional<RecruitmentComment> findRecruitmentComment = recruitmentCommentRepository
                .findById(savedRecruitmentComment.getId());

        // then
        assertThatThrownBy(() -> findRecruitmentComment.get()).isInstanceOf(NoSuchElementException.class);
    }

}
