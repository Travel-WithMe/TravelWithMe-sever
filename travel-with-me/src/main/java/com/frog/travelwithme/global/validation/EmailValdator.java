package com.frog.travelwithme.global.validation;

import com.frog.travelwithme.global.validation.CustomAnnotationCollection.CustomEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CustomEmailValdator 설명: CustomEmail 애너테이션 인터페이스 구현
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/20
 **/
public class EmailValdator implements ConstraintValidator<CustomEmail, String> {

    private static final String EMAIL_PATTERN =
            "^(?=.{1,64}@)[A-Za-z0-9-]+(.[A-Za-z0-9-]+)@" +
                    "[^-][A-Za-z0-9-]+(.[A-Za-z0-9-]+)(.[A-Za-z]{2,})$";
    private Pattern pattern;

    // 패턴 컴파일
    @Override
    public void initialize(CustomEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    // 전달받은 email이 유효한지 검증
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
