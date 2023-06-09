package com.frog.travelwithme.config;

import com.amazonaws.services.s3.AmazonS3;
import com.frog.travelwithme.global.config.AwsS3Config;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockAwsS3Config extends AwsS3Config {

    @Bean
    @Primary
    @Override
    public AmazonS3 amazonS3Client() {
        return Mockito.mock(AmazonS3.class);
    }
}
