package com.frog.travelwithme.global.enums;

import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnumCollection {

    @AllArgsConstructor
    public enum MemberStatus implements EnumType {
        ACTIVE("활동상태"),
        SLEEP("휴면상태"),
        QUIT("탈퇴상태");

        private final String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

    @AllArgsConstructor
    public enum OAuthStatus implements EnumType {
        NORMAL("일반"),
        OAUTH("소셜");

        private final String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

    @AllArgsConstructor
    public enum Roles implements EnumType {
        USER, ADMIN;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.name();
        }
    }

    @AllArgsConstructor
    public enum MatchingStatus implements EnumType {
        REQUEST("승인요청 상태"),
        APPROVE("승인완료 상태"),
        REJECT("거절 상태"),
        CANCEL("취소 상태");

        private final String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

    @AllArgsConstructor
    public enum RecruitmentStatus implements EnumType {
        IN_PROGRESS("동행 모집중 상태"),
        END("동행 모집종료 상태");

        private final String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

    @AllArgsConstructor
    public enum ResponseBody implements EnumType {
        NEW_REQUEST_MATCHING("동행 매칭신청이 완료되었습니다."),
        RETRY_REQUEST_MATCHING("동행 재신청이 완료되었습니다."),
        CANCEL_MATCHING("동행 매칭신청이 취소되었습니다."),
        APPROVE_MATCHING("동행 매칭신청이 승인되었습니다."),
        REJECT_MATCHING("동행 매칭신청이 거절되었습니다."),
        SUCCESS_FEED_LIKE("피드를 좋아요했습니다."),
        SUCCESS_CANCEL_FEED_LIKE("피드 좋아요를 취소했습니다.");

        private final String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

    @AllArgsConstructor
    public enum Gender implements EnumType {
        MALE("남자"),
        FEMALE("여자");

        private final String description;

        public static Gender from(String sub) {
            if (sub == null) {
                return null;
            }
            for (Gender gender : Gender.values()) {
                if (gender.getDescription().equals(sub)) {
                    return gender;
                }
            }
            log.debug("EnumCollection.Gender.from() exception occur sub: {}", sub);
            throw new BusinessLogicException(ExceptionCode.INVALID_GENDER);
        }

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

}
