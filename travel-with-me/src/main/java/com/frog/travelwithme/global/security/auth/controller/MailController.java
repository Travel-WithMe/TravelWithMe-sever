package com.frog.travelwithme.global.security.auth.controller;

import com.frog.travelwithme.global.security.auth.service.MailService;
import com.frog.travelwithme.global.validation.CustomAnnotationCollection.CustomEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/20
 **/
@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/verification-requests")
    public ResponseEntity sendMessage(@Valid @CustomEmail @RequestParam("email") String email)
            throws NoSuchAlgorithmException {
        mailService.sendEmail(email);
        String response = "이메일 인증 번호가 전송되었습니다. 인증 번호의 유효시간은 30분입니다.";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/verifications")
    public ResponseEntity verificationEmail(@Valid @CustomEmail @RequestParam("email") String email,
                                           @RequestParam("code") String authCode) {
        String response;
        if (mailService.verifiedCode(email, authCode)) {
            response = "이메일 인증에 성공했습니다.";
        } else response = "이메일 인증에 실패했습니다.";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
