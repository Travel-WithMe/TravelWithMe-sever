package com.frog.travelwithme.global.utils;

import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
public class TimeUtils {

    public static final LocalTime LOCAL_TIME = LocalTime.of(0, 0);

    public static LocalDate stringToLocalDate(String localDate) {
        try {
            return LocalDate.parse(localDate);
        } catch (RuntimeException e) {
            log.debug("TimeChangeUtils.stringToLocalDate exception occur localDate: {}", localDate);
            throw new BusinessLogicException(ExceptionCode.STRING_IS_NOT_LOCAL_DATE_FORMAT);
        }
    }

    public static LocalDateTime stringToLocalDateTime(String localDate) {
            return LocalDateTime.of(stringToLocalDate(localDate), LOCAL_TIME);
    }

    public static LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LOCAL_TIME);
    }

    public static LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    public static String localDateToString(LocalDate localDate) {
        return localDate.toString();
    }
}
