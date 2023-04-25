package com.frog.travelwithme.global.enums;

import lombok.AllArgsConstructor;

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
    public enum BuddyMatchingStatus implements EnumType {
        WAIT("승인대기 상태"),
        APPROVE("승인완료 상태"),
        REJECT("거절 상태");

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
    public enum BuddyRecruitmentStatus implements EnumType {
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
        RETRY_REQUEST_MATCHING("동행 재신청이 완료되었습니다.");

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
}
