package com.frog.travelwithme.global.security.auth.jwt;

import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import com.frog.travelwithme.global.security.auth.dto.TokenDto;
import com.frog.travelwithme.global.security.auth.userdetails.CustomUserDetails;
import com.frog.travelwithme.global.security.auth.utils.Responder;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtTokenProvider 설명: JWT 토큰 생성, 복호화 및 정보 추출, 유효성 검증.
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/03/27
 **/
@Slf4j
@Component
public class JwtTokenProvider {
    public static final String BEARER_TYPE = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";

    @Getter
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;
    private Key key;

    // Bean 등록후 Key SecretKey HS256 decode
    @PostConstruct
    public void init() {
        String base64EncodedSecretKey = encodeBase64SecretKey(this.secretKey);
        this.key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
    }

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(CustomUserDetails customUserDetails) {
        Date accessTokenExpiresIn = getTokenExpiration(accessTokenExpirationMinutes);
        Date refreshTokenExpiresIn = getTokenExpiration(refreshTokenExpirationMinutes);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", customUserDetails.getRole());

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(customUserDetails.getEmail())
                .setExpiration(accessTokenExpiresIn)
                .setIssuedAt(Calendar.getInstance().getTime())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(customUserDetails.getEmail())
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .authorizationType(AUTHORIZATION_HEADER)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰 정보를 반환
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new BusinessLogicException(ExceptionCode.NO_ACCESS_TOKEN);
        }

        String authority = claims.get("role").toString();

        CustomUserDetails customUserDetails = CustomUserDetails.of(
                claims.getSubject(),
                authority);

        log.info("# AuthMember.getRoles 권한 체크 = {}", customUserDetails.getAuthorities().toString());

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }

    // 토큰 검증
    public boolean validateToken(String token, HttpServletResponse response) {
        try {
            parseClaims(token);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token");
            log.trace("Invalid JWT token trace = {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
            log.trace("Expired JWT token trace = {}", e);
            Responder.sendErrorResponse(response, ExceptionCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
            log.trace("Unsupported JWT token trace = {}", e);
            Responder.sendErrorResponse(response, ExceptionCode.TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.");
            log.trace("JWT claims string is empty trace = {}", e);
            Responder.sendErrorResponse(response, ExceptionCode.TOKEN_ILLEGAL_ARGUMENT);
        }
        return true;
    }

    private Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    // Token 복호화 및 예외 발생(토큰 만료, 시그니처 오류)시 Claims 객체가 안만들어짐.
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void accessTokenSetHeader(String accessToken, HttpServletResponse response) {
        String headerValue = BEARER_PREFIX + accessToken;
        response.setHeader(AUTHORIZATION_HEADER, headerValue);
    }

    public void refresshTokenSetHeader(String refreshToken, HttpServletResponse response) {
        response.setHeader("Refresh", refreshToken);
    }

    // Request Header에 Access Token 정보를 추출하는 메서드
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Request Header에 Refresh Token 정보를 추출하는 메서드
    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }
}
