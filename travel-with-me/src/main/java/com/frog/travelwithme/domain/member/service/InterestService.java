package com.frog.travelwithme.domain.member.service;

import com.frog.travelwithme.domain.member.entity.Interest;
import com.frog.travelwithme.domain.member.repository.InterestRepository;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Member 설명: 회원 데이터 관리
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/05/29
 **/
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    public List<Interest> findInterests(List<String> interestTypes) {
        if (CollectionUtils.isEmpty(interestTypes)) {
            return new ArrayList<>();
        } else {
            return interestTypes.stream()
                    .map(this::findByType)
                    .collect(Collectors.toList());
        }
    }

    private Interest findByType(String interestType) {
        return interestRepository.findByType(interestType)
                .orElseThrow(() -> {
                    log.debug("InterestService.findByType exception occur interestType : {}",
                            interestType);
                    throw new BusinessLogicException(ExceptionCode.MEMBER_INTEREST_NOT_FOUND);
                });
    }
}
