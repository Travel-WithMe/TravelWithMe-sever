package com.frog.travelwithme.unit.security;

import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.security.auth.controller.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.jwt.JwtTokenProvider;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/01
 **/
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private String secretKey;
    private String base64EncodedSecretKey;

    @BeforeAll
    public void init() {
        jwtTokenProvider = new JwtTokenProvider();
        secretKey = "testSecretKey20230327testSecretKey20230327testSecretKey20230327";
        base64EncodedSecretKey = jwtTokenProvider.encodeBase64SecretKey(secretKey);
    }

    @DisplayName("Secret Key 암호화")
    @Test
    void jwtTokenProviderTest1() throws Exception {
        log.info(base64EncodedSecretKey);

        // then
        assertNotEquals(secretKey, base64EncodedSecretKey);
    }

    @DisplayName("Secret Key 복호화")
    @Test
    void jwtTokenProviderTest2() throws Exception {
        log.info(base64EncodedSecretKey);
        String decodeSecretKey = new String(Decoders.BASE64.decode(base64EncodedSecretKey));

        // then
        assertThat(secretKey, is(decodeSecretKey));
    }

    @DisplayName("Access Token 생성")
    @Test
    void jwtTokenProviderTest3() throws Exception {
        // when
        String accessToken = this.getAccessToken();
        log.info("accessToken : " + accessToken);

        // then
        assertNotNull(accessToken);
    }

    @DisplayName("Refresh Token 생성")
    @Test
    void jwtTokenProviderTest4() throws Exception {
        // when
        String refreshToken = this.getRefreshToken();
        log.info("refreshToken : " + refreshToken);

        // then
        assertNotNull(refreshToken);
    }

    @DisplayName("JWS를 검증할 때 예외가 발생하지 않음")
    @Test
    void jwtTokenProviderTest5() {
        HttpServletResponse response = new Response();
        String accessToken = this.getAccessToken();

        assertDoesNotThrow(() -> jwtTokenProvider.validateToken(accessToken, response));
    }

    @DisplayName("JWS를 검증할 때 예외가 발생함")
    @Test
    void jwtTokenProviderTest6() throws InterruptedException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String accessToken = getAccessToken();

        // when // then
        assertDoesNotThrow(() -> jwtTokenProvider.validateToken(accessToken, response));
        await().pollDelay(Duration.ofMillis(6000)).untilAsserted(
                () -> {
                    assertThrows(BusinessLogicException.class, () -> jwtTokenProvider.validateToken(
                            accessToken, response));
                });
    }

    private String getAccessToken() {
        CustomUserDetails userDetails = CustomUserDetails.of("email", "USER");
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);

        return tokenDto.getAccessToken();
    }

    private String getRefreshToken() {
        CustomUserDetails userDetails = CustomUserDetails.of("email", "USER");
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(userDetails);

        return tokenDto.getRefreshToken();
    }
}