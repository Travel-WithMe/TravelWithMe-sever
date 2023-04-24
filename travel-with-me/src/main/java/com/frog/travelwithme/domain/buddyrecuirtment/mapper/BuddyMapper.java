package com.frog.travelwithme.domain.buddyrecuirtment.mapper;

import com.frog.travelwithme.domain.buddyrecuirtment.common.DeletionEntity;
import com.frog.travelwithme.domain.buddyrecuirtment.controller.dto.BuddyDto;
import com.frog.travelwithme.domain.buddyrecuirtment.entity.BuddyRecruitment;
import com.frog.travelwithme.global.utils.TimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

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
        String travelStartDate = postRecruitmentDto.getTravelStartDate();
        String travelEndDate = postRecruitmentDto.getTravelEndDate();
        DeletionEntity deletionEntity = new DeletionEntity();

        return BuddyRecruitment.builder()
                .title(postRecruitmentDto.getTitle())
                .content(postRecruitmentDto.getContent())
                .travelNationality(postRecruitmentDto.getTravelNationality())
                .travelStartDate(TimeUtils.stringToLocalDateTime(travelStartDate))
                .travelEndDate(TimeUtils.stringToLocalDateTime(travelEndDate))
                .buddyRecruitmentStatus(BuddyRecruitmentStatus.IN_PROGRESS)
                .deletionEntity(deletionEntity)
                .build();
    };

    default BuddyDto.PostResponseRecruitment toPostResponseRecruitmentDto(BuddyRecruitment buddyRecruitment){
        if(buddyRecruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = buddyRecruitment.getTravelStartDate();
        LocalDateTime travelEndDate = buddyRecruitment.getTravelEndDate();

        return BuddyDto.PostResponseRecruitment.builder()
                .title(buddyRecruitment.getTitle())
                .content(buddyRecruitment.getContent())
                .travelNationality(buddyRecruitment.getTravelNationality())
                .travelStartDate(TimeUtils.localDateTimeToLocalDate(travelStartDate))
                .travelEndDate(TimeUtils.localDateTimeToLocalDate(travelEndDate))
                .viewCount(buddyRecruitment.getViewCount())
                .commentCount(buddyRecruitment.getCommentCount())
                .nickname(buddyRecruitment.getMember().getNickname())
                .memberImage(buddyRecruitment.getMember().getImage())
                .build();
    };

    default BuddyDto.PatchResponseRecruitment toPatchResponseRecruitmentDto(BuddyRecruitment buddyRecruitment){
        if(buddyRecruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = buddyRecruitment.getTravelStartDate();
        LocalDateTime travelEndDate = buddyRecruitment.getTravelEndDate();

        return BuddyDto.PatchResponseRecruitment.builder()
                .title(buddyRecruitment.getTitle())
                .content(buddyRecruitment.getContent())
                .travelNationality(buddyRecruitment.getTravelNationality())
                .travelStartDate(TimeUtils.localDateTimeToLocalDate(travelStartDate))
                .travelEndDate(TimeUtils.localDateTimeToLocalDate(travelEndDate))
                .build();
    };

    default BuddyDto.GetResponseRecruitment toGetResponseRecruitmentDto(BuddyRecruitment buddyRecruitment) {
        if (buddyRecruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = buddyRecruitment.getTravelStartDate();
        LocalDateTime travelEndDate = buddyRecruitment.getTravelEndDate();

        return BuddyDto.GetResponseRecruitment.builder()
                .title(buddyRecruitment.getTitle())
                .content(buddyRecruitment.getContent())
                .travelNationality(buddyRecruitment.getTravelNationality())
                .travelStartDate(TimeUtils.localDateTimeToLocalDate(travelStartDate))
                .travelEndDate(TimeUtils.localDateTimeToLocalDate(travelEndDate))
                .viewCount(buddyRecruitment.getViewCount())
                .commentCount(buddyRecruitment.getCommentCount())
                .nickname(buddyRecruitment.getMember().getNickname())
                .memberImage(buddyRecruitment.getMember().getImage())
                .build();
    }

}
