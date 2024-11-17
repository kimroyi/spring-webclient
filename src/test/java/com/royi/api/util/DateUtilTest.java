package com.royi.api.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

    @Test
    void formatTodayDateWithTimeTest() {
        // 오늘 날짜 가져오기
        String todayDate = LocalDate.now().toString(); // "yyyy-MM-dd" 형식
        String time = "23:59:59"; // 고정된 시간

        String todayDateTime = DateUtil.formatDateTime(todayDate, time, "yyyy-MM-dd HH:mm:ss");

        assertThat(todayDateTime).isEqualTo("2024-11-17 23:59:59");
    }
}