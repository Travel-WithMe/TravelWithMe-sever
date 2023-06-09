package com.frog.travelwithme.unit.domain.buddy.repository;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.repository.MatchingRepository;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.MemberRepository;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.buddy.repository.RecruitmentRepository;
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

import static com.frog.travelwithme.global.enums.EnumCollection.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
@ExtendWith(SpringExtension.class)
class RecruitmentRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected MatchingRepository matchingRepository;

    @Test
    @DisplayName("동행 레포지토리 저장")
    void recruitmentRepositoryTest1() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();

        // when
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // then
        assertAll(
                () -> assertEquals(saveRecruitment.getTitle(), recruitment.getTitle()),
                () -> assertEquals(saveRecruitment.getContent(), recruitment.getContent()),
                () -> assertEquals(saveRecruitment.getTravelNationality(), recruitment.getTravelNationality())
        );
    }

    @Test
    @DisplayName("동행 레포지토리 수정")
    void recruitmentRepositoryTest2() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        entityManager.clear();
        entityManager.flush();

        Recruitment findRecruitment = recruitmentRepository.findById(saveRecruitment.getId()).get();

        // when
        findRecruitment.end();

        // then
        assertThat(findRecruitment.getId()).isEqualTo(saveRecruitment.getId());
        assertThat(findRecruitment.getRecruitmentStatus()) // Expect : END
                .isNotEqualTo(recruitment.getRecruitmentStatus()); // Result : IN_PROGRESS
    }

    @Test
    @DisplayName("동행 레포지토리 조회")
    void recruitmentRepositoryTest3() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // when
        Recruitment findRecruitment = recruitmentRepository.findById(saveRecruitment.getId()).get();

        // then
        assertAll(
                () -> assertEquals(findRecruitment.getTitle(), recruitment.getTitle()),
                () -> assertEquals(findRecruitment.getContent(), recruitment.getContent()),
                () -> assertEquals(findRecruitment.getTravelNationality(), recruitment.getTravelNationality())
        );
    }

    @Test
    @DisplayName("동행 레포지토리 삭제")
    void recruitmentRepositoryTest4() {
        // given
        Recruitment recruitment = StubData.MockRecruitment.getRecruitment();
        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

        // when
        recruitmentRepository.delete(saveRecruitment);

        Optional<Recruitment> findRecruitment = recruitmentRepository
                .findById(saveRecruitment.getId());

        // then
        assertThatThrownBy(() -> findRecruitment.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("동행글 id 조회(Matching Join) : Querydsl")
    void recruitmentRepositoryTest5() {
        // given
        Member writer = StubData.MockMember.getMember();
        Member member1 = StubData.MockMember.getMemberByEmailAndNickname("kkd718@naver.com","멤버1");
        Member member2 = StubData.MockMember.getMemberByEmailAndNickname("dhfif718@naver.com", "멤버2");
        Member member3 = StubData.MockMember.getMemberByEmailAndNickname("hama@naver.com", "멤버3");

        Member savedWriter = memberRepository.save(writer);
        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);
        Member saveMember3 = memberRepository.save(member3);

        Recruitment recruitment1 = StubData.MockRecruitment.getRecruitment();
        recruitment1.addMember(savedWriter);
        Recruitment saveRecruitment1 = recruitmentRepository.save(recruitment1);

        Matching matching1 = StubData.MockMatching.getMatching();
        matching1.addRecruitment(saveRecruitment1);
        matching1.addMember(saveMember1);

        Matching matching2 = StubData.MockMatching.getMatching();
        matching2.addRecruitment(saveRecruitment1);
        matching2.addMember(saveMember2);

        Matching matching3 = StubData.MockMatching.getMatching();
        matching3.addRecruitment(saveRecruitment1);
        matching3.addMember(saveMember3);
        matching3.approve();

        Matching savedMatching1 = matchingRepository.save(matching1);
        Matching savedMatching2 = matchingRepository.save(matching2);
        Matching savedMatching3 = matchingRepository.save(matching3);

        saveRecruitment1.addMatching(savedMatching1);
        saveRecruitment1.addMatching(savedMatching2);
        saveRecruitment1.addMatching(savedMatching3);

        entityManager.flush();
        entityManager.clear();
        log.info("1차 캐시 clear");

        // when
        Recruitment findRecruitment = recruitmentRepository
                .findRecruitmentByIdAndMatchingStatus(saveRecruitment1.getId(), MatchingStatus.REQUEST).get();

        for (Matching matching : findRecruitment.getMatchingList()) {
            matching.getMember();
        }

        // then
        assertAll(
                () -> assertEquals(findRecruitment.getId(), recruitment1.getId()),
                () -> assertEquals(findRecruitment.getMatchingList().get(0).getMember().getNickname(), member1.getNickname()),
                () -> assertEquals(findRecruitment.getMatchingList().get(1).getMember().getNickname(), member2.getNickname()),
                () -> assertEquals(findRecruitment.getMatchingList().get(0).getMember().getEmail(), member1.getEmail()),
                () -> assertEquals(findRecruitment.getMatchingList().get(1).getMember().getEmail(), member2.getEmail())
        );
        assertThatThrownBy(() -> findRecruitment.getMatchingList().get(2)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @DisplayName("동행글 id 조회 시 매칭이 없는 경우 (Matching Join) : Querydsl")
    void recruitmentRepositoryTest6() {
        // given
        Member writer = StubData.MockMember.getMember();
        Member member1 = StubData.MockMember.getMemberByEmailAndNickname("kkd718@naver.com","멤버1");
        Member member2 = StubData.MockMember.getMemberByEmailAndNickname("dhfif718@naver.com", "멤버2");
        Member member3 = StubData.MockMember.getMemberByEmailAndNickname("hama@naver.com", "멤버3");

        Member savedWriter = memberRepository.save(writer);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Recruitment recruitment1 = StubData.MockRecruitment.getRecruitment();
        recruitment1.addMember(savedWriter);
        Recruitment saveRecruitment1 = recruitmentRepository.save(recruitment1);

        entityManager.flush();
        entityManager.clear();
        log.info("1차 캐시 clear");

        // when
        Optional<Recruitment> findRecruitment = recruitmentRepository
                .findRecruitmentByIdAndMatchingStatus(saveRecruitment1.getId(), MatchingStatus.REQUEST);

        // then
        assertThatThrownBy(() -> findRecruitment.get()).isInstanceOf(NoSuchElementException.class);
    }
}
