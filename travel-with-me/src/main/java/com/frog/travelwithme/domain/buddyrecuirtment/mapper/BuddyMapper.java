package com.frog.travelwithme.domain.buddyrecuirtment.mapper;

import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.global.enums.EnumCollection;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.frog.travelwithme.global.enums.EnumCollection.*;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BuddyMapper {
    default BuddyRecruitment toEntity(BuddyDto.PostRecruitment postRecruitmentDto) {

        if(postRecruitmentDto == null) {
            return null;
        }
        LocalDate travelStartDate = LocalDate.parse(postRecruitmentDto.getTravelStartDate());
        LocalDate travelEndDate = LocalDate.parse(postRecruitmentDto.getTravelEndDate());
        LocalTime localTime = LocalTime.of(0, 0);

        return BuddyRecruitment.builder()
                .title(postRecruitmentDto.getTitle())
                .content(postRecruitmentDto.getContent())
                .travelNationality(postRecruitmentDto.getTravelNationality())
                .travelStartDate(LocalDateTime.of(travelStartDate, localTime))
                .travelEndDate(LocalDateTime.of(travelEndDate, localTime))
                .buddyRecruitmentStatus(BuddyRecruitmentStatus.IN_PROGRESS)
                .build();
    };

    default BuddyDto.ResponseRecruitment toDto(BuddyRecruitment buddyRecruitment){
        if(buddyRecruitment == null) {
            return null;
        }

        return BuddyDto.ResponseRecruitment.builder()
                .title(buddyRecruitment.getTitle())
                .content(buddyRecruitment.getContent())
                .travelNationality(buddyRecruitment.getTravelNationality())
                .travelStartDate(buddyRecruitment.getTravelStartDate().toLocalDate())
                .travelEndDate(buddyRecruitment.getTravelEndDate().toLocalDate())
                .viewCount(buddyRecruitment.getViewCount())
                .commentCount(buddyRecruitment.getCommentCount())
                .nickname(buddyRecruitment.getMember().getNickname())
                .memberImage(buddyRecruitment.getMember().getImage())
                .build();
    };

}
