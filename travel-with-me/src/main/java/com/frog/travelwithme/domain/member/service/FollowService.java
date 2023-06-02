package com.frog.travelwithme.domain.member.service;

import com.frog.travelwithme.domain.member.entity.Follow;
import com.frog.travelwithme.domain.member.entity.Member;
import com.frog.travelwithme.domain.member.repository.FollowRepository;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * FollowService 설명: 팔로우 로직 처리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/06/02
 **/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public void follow(Member follower, Member following) {
        this.checkDuplicatedFollow(follower.getId(), following.getId());
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        Follow saveFollow = followRepository.save(follow);
        follower.addFollowing(saveFollow);
        following.addFollower(saveFollow);
    }

    public void unfollow(Member follower, Member following) {
        Follow follow = this.findByFollowerIdAndFollowingId(follower.getId(), following.getId());
        followRepository.deleteById(follow.getId());
    }

    private void checkDuplicatedFollow(Long followerId, Long followingId) {
        Optional<Follow> optionalFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (optionalFollow.isPresent()) {
            log.debug("FollowService.checkDuplicatedFollow exception occur " +
                    "followerId : {}, followingId : {}", followerId, followingId);
            throw new BusinessLogicException(ExceptionCode.FOLLOW_EXISTS);
        }
    }

    private Follow findByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> {
                    log.debug("FollowService.findByFollowerIdAndFollowingId exception occur" +
                            " followerId : {}, followingId : {}", followerId, followingId);
                    throw new BusinessLogicException(ExceptionCode.FOLLOW_NOT_FOUND);
                });
    }
}
