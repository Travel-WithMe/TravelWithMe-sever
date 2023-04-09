package com.frog.travelwithme.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoderConfig 설명: PasswordEncoder Bean 생성
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/31
 **/
@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
