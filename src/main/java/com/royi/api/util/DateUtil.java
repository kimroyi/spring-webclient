package com.royi.api.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateUtil {

    private static final DateTimeFormatter DEFAULT_INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DEFAULT_OUTPUT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // 날짜와 시간을 조합하고 지정된 형식으로 포맷
    public static String formatDateTime(String date, String time, DateTimeFormatter formatter)
    {
        if ( date == null || time == null )
        {
            log.error("날짜 또는 시간 입력이 null입니다");
            return "";
        }

        try
        {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date, DateTimeFormatter.ISO_DATE), LocalTime.parse(time, DateTimeFormatter.ISO_TIME));
            return dateTime.format(formatter);
        }
        catch (DateTimeParseException e)
        {
            log.error("Error parsing date or time: {}", e.getMessage());
            return "";
        }
    }

    // 문자열 패턴으로 사용자 정의 형식 포맷
    public static String formatDateTime(String date, String time, String pattern)
    {
        return formatDateTime(date, time, DateTimeFormatter.ofPattern(pattern));
    }

    // 지정된 형식의 단일 날짜 시간 문자열 형식 지정
    public static String formatDateTime(String dateTime, DateTimeFormatter formatter)
    {
        if ( dateTime == null )
        {
            log.error("DateTime input is null");
            return "";
        }

        try
        {
            LocalDateTime localDateTime = parseDateTime(dateTime);
            return localDateTime.format(formatter);
        }
        catch (DateTimeParseException e)
        {
            log.error("Error parsing dateTime: {}", e.getMessage());
            return "";
        }
    }

    // 문자열 패턴으로 사용자 정의 형식 포맷
    public static String formatDateTime(String dateTime, String pattern)
    {
        return formatDateTime(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    // 날짜 및 시간 문자열을 LocalDateTime 으로 결합
    public static LocalDateTime combineDateAndTime(String date, String time)
    {
        return LocalDateTime.of(LocalDate.parse(date, DateTimeFormatter.ISO_DATE), LocalTime.parse(time, DateTimeFormatter.ISO_TIME));
    }

    // 기본 입력 형식을 사용하여 날짜 시간 문자열 구문 분석
    private static LocalDateTime parseDateTime(String dateTime)
    {
        return LocalDateTime.parse(dateTime, DEFAULT_INPUT_FORMATTER);
    }

    // 기본 출력 형식을 사용하여 LocalDateTime을 포맷
    private static String formatLocalDateTime(LocalDateTime dateTime)
    {
        return dateTime.format(DEFAULT_OUTPUT_FORMATTER);
    }
}
