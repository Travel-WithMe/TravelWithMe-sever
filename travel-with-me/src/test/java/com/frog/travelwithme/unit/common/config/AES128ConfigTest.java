package com.frog.travelwithme.unit.common.config;

import com.frog.travelwithme.global.config.AES128Config;
import com.frog.travelwithme.global.exception.BusinessLogicException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class AES128ConfigTest {

    @Autowired
    private AES128Config aes128Config;

    @Test
    @DisplayName("Aes128 암호화 및 복호화 테스트")
    void aes128Test() {
        String text = "this is test";
        String enc = aes128Config.encryptAes(text);
        String dec = aes128Config.decryptAes(enc);
        log.info("enc = {}", enc);
        log.info("dec = {}", dec);

        assertThat(dec).isEqualTo(text);
    }

    @Test
    @DisplayName("Secret Key 검증 테스트")
    void secretKeyInvalidTest() {
        Assertions.assertThrows(BusinessLogicException.class, () ->  aes128Config.validation("failKey"));
    }
}