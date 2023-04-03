package com.frog.travelwithme.global.security.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TokenDto 설명: 클라이언트에 토큰 전송
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/27
 **/
@Data
@Builder
public class TokenDto {
    private final String grantType;
    private final String authorizationType;
    private final String accessToken;
    private final String refreshToken;
    private final Long accessTokenExpiresIn;
}
