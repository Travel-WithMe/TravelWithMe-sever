package com.frog.travelwithme.common;

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
}
