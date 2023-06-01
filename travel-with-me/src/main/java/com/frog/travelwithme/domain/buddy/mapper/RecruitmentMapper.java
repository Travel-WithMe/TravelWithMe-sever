package com.frog.travelwithme.domain.buddy.mapper;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.common.DeletionEntity;
import com.frog.travelwithme.domain.buddy.controller.dto.BuddyDto;
import com.frog.travelwithme.global.utils.TimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.frog.travelwithme.global.enums.EnumCollection.RecruitmentStatus;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/11
 **/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecruitmentMapper {
    default Recruitment toEntity(BuddyDto.RecruitmentPost recruitmentPostDto) {

        if(recruitmentPostDto == null) {
            return null;
        }
        String travelStartDate = recruitmentPostDto.getTravelStartDate();
        String travelEndDate = recruitmentPostDto.getTravelEndDate();
        DeletionEntity deletionEntity = new DeletionEntity();

        return Recruitment.builder()
                .title(recruitmentPostDto.getTitle())
                .content(recruitmentPostDto.getContent())
                .travelNationality(recruitmentPostDto.getTravelNationality())
                .travelStartDate(TimeUtils.stringToLocalDateTime(travelStartDate))
                .travelEndDate(TimeUtils.stringToLocalDateTime(travelEndDate))
                .recruitmentStatus(RecruitmentStatus.IN_PROGRESS)
                .deletionEntity(deletionEntity)
                .build();
    }

    default BuddyDto.RecruitmentPostResponse toPostResponseBuddyDto(Recruitment recruitment){
        if(recruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = recruitment.getTravelStartDate();
        LocalDateTime travelEndDate = recruitment.getTravelEndDate();

        return BuddyDto.RecruitmentPostResponse.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .content(recruitment.getContent())
                .travelNationality(recruitment.getTravelNationality())
                .travelStartDate(TimeUtils.localDateTimeToLocalDate(travelStartDate))
                .travelEndDate(TimeUtils.localDateTimeToLocalDate(travelEndDate))
                .viewCount(recruitment.getViewCount())
                .commentCount(recruitment.getCommentCount())
                .nickname(recruitment.getMember().getNickname())
                .memberImage(recruitment.getMember().getImage())
                .build();
    };

    default BuddyDto.RecruitmentPatchResponse toPatchResponseBuddyDto(Recruitment recruitment){
        if(recruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = recruitment.getTravelStartDate();
        LocalDateTime travelEndDate = recruitment.getTravelEndDate();

        return BuddyDto.RecruitmentPatchResponse.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .content(recruitment.getContent())
                .travelNationality(recruitment.getTravelNationality())
                .travelStartDate(TimeUtils.localDateTimeToLocalDate(travelStartDate))
                .travelEndDate(TimeUtils.localDateTimeToLocalDate(travelEndDate))
                .build();
    };

    default BuddyDto.RecruitmentGetResponse toGetResponseRecruitmentBuddyDto(Recruitment recruitment) {
        if (recruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = recruitment.getTravelStartDate();
        LocalDateTime travelEndDate = recruitment.getTravelEndDate();

        return BuddyDto.RecruitmentGetResponse.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .content(recruitment.getContent())
                .travelNationality(recruitment.getTravelNationality())
                .travelStartDate(TimeUtils.localDateTimeToLocalDate(travelStartDate))
                .travelEndDate(TimeUtils.localDateTimeToLocalDate(travelEndDate))
                .viewCount(recruitment.getViewCount())
                .commentCount(recruitment.getCommentCount())
                .nickname(recruitment.getMember().getNickname())
                .memberImage(recruitment.getMember().getImage())
                .build();
    }

    default BuddyDto.MatchingMemberResponse toMatchingMemberResponseBuddyDto(Matching matching) {
        if(matching == null) {
            return null;
        }

        return BuddyDto.MatchingMemberResponse.builder()
                .id(matching.getMember().getId())
                .nickname(matching.getMember().getNickname())
                .image(matching.getMember().getImage())
                .build();
    }

    default List<BuddyDto.MatchingMemberResponse> toMatchingMemberResponseBuddyDtoList(List<Matching> matchingList) {
        if(matchingList == null) {
            return null;
        }

        List<BuddyDto.MatchingMemberResponse> matchingMemberResponseList = new ArrayList<>();

        for (Matching matching : matchingList) {
            matchingMemberResponseList.add(this.toMatchingMemberResponseBuddyDto(matching));
        }
        return matchingMemberResponseList;
    }

}
