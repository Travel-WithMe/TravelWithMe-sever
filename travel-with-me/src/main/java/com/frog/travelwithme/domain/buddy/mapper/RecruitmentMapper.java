package com.frog.travelwithme.domain.buddy.mapper;

import com.frog.travelwithme.domain.buddy.entity.Matching;
import com.frog.travelwithme.domain.buddy.entity.Recruitment;
import com.frog.travelwithme.domain.common.DeletionEntity;
import com.frog.travelwithme.domain.buddy.controller.dto.RecruitmentDto;
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
    default Recruitment toEntity(RecruitmentDto.Post postDto) {

        if(postDto == null) {
            return null;
        }
        String travelStartDate = postDto.getTravelStartDate();
        String travelEndDate = postDto.getTravelEndDate();
        DeletionEntity deletionEntity = new DeletionEntity();

        return Recruitment.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .travelNationality(postDto.getTravelNationality())
                .travelStartDate(TimeUtils.stringToLocalDateTime(travelStartDate))
                .travelEndDate(TimeUtils.stringToLocalDateTime(travelEndDate))
                .recruitmentStatus(RecruitmentStatus.IN_PROGRESS)
                .deletionEntity(deletionEntity)
                .build();
    }

    default RecruitmentDto.PostResponse toPostResponseRecruitmentDto(Recruitment recruitment){
        if(recruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = recruitment.getTravelStartDate();
        LocalDateTime travelEndDate = recruitment.getTravelEndDate();

        return RecruitmentDto.PostResponse.builder()
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

    default RecruitmentDto.PatchResponse toPatchResponseRecruitmentDto(Recruitment recruitment){
        if(recruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = recruitment.getTravelStartDate();
        LocalDateTime travelEndDate = recruitment.getTravelEndDate();

        return RecruitmentDto.PatchResponse.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .content(recruitment.getContent())
                .travelNationality(recruitment.getTravelNationality())
                .travelStartDate(TimeUtils.localDateTimeToLocalDate(travelStartDate))
                .travelEndDate(TimeUtils.localDateTimeToLocalDate(travelEndDate))
                .build();
    };

    default RecruitmentDto.GetResponse toGetResponseRecruitmentDto(Recruitment recruitment) {
        if (recruitment == null) {
            return null;
        }

        LocalDateTime travelStartDate = recruitment.getTravelStartDate();
        LocalDateTime travelEndDate = recruitment.getTravelEndDate();

        return RecruitmentDto.GetResponse.builder()
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

    default RecruitmentDto.MatchingRequestMemberResponse toMatchingRequestMember(Matching matching) {
        if(matching == null) {
            return null;
        }

        return RecruitmentDto.MatchingRequestMemberResponse.builder()
                .id(matching.getMember().getId())
                .nickname(matching.getMember().getNickname())
                .image(matching.getMember().getImage())
                .build();
    }

    default List<RecruitmentDto.MatchingRequestMemberResponse> toMatchingRequestMemberList(List<Matching> matchingList) {
        if(matchingList == null) {
            return null;
        }

        List<RecruitmentDto.MatchingRequestMemberResponse> matchingRequestMemberResponseList = new ArrayList<>();

        for (Matching matching : matchingList) {
            matchingRequestMemberResponseList.add(this.toMatchingRequestMember(matching));
        }
        return matchingRequestMemberResponseList;
    }

}
