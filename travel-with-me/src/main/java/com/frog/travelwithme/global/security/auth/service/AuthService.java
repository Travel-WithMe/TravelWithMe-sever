package com.frog.travelwithme.global.security.auth.service;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/05
 **/
public interface AuthService {

    String reissueAccessToken(String encryptedRefreshToken);

    void logout(String encryptedRefreshToken, String accessToken);
}
