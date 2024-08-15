package com.royi.api.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TimeRangeTest {

    @Test
    void getTimeRanges() {
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T23:59:59");
        int intervalHours = 12;

        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange.getTimeRanges(startTime, endTime, intervalHours, "ASC");

        for (Pair<LocalDateTime, LocalDateTime> timeRange : timeRanges) {
            LocalDateTime start = timeRange.getFirst();
            LocalDateTime end = timeRange.getSecond();

            System.out.println("해당 기간: " + start + " to " + end);
        }
    }

    @Test
    @DisplayName("오름차순 테스트 케이스")
    void getTimeRanges_asc_normal() {
        // Given
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T23:59:59");
        int intervalHours = 12;

        // When
        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange.getTimeRanges(startTime, endTime, intervalHours, "ASC");

        // Then
        assertThat(timeRanges.size()).isEqualTo(10);
        assertThat(timeRanges.get(0).getFirst().toString()).isEqualTo(startTime.toString());
    }

    @Test
    @DisplayName("내림차순 테스트 케이스")
    void getTimeRanges_desc_normal() {
        // Given
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T23:59:59");
        int intervalHours = 12;

        // When
        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange.getTimeRanges(startTime, endTime, intervalHours, "DESC");

        // Then
        assertThat(timeRanges.size()).isEqualTo(10);
        assertThat(timeRanges.get(0).getSecond().toString()).isEqualTo(endTime.toString());
    }

    @Test
    @DisplayName("파라미터 예외 테스트 케이스")
    void getTimeRanges_invalidOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            TimeRange.getTimeRanges(LocalDateTime.now(), LocalDateTime.now().plusDays(1), 12, "INVALID");
        });
    }

    @Test
    @DisplayName("시작 시간이 끝 시간보다 큰 경우 테스트 케이스")
    void getTimeRanges_startTimeAfterEndTime() {
        LocalDateTime startTime = LocalDateTime.parse("2024-08-04T23:59:59");
        LocalDateTime endTime = LocalDateTime.parse("2024-07-31T00:00:00");
        int intervalHours = 12;

        // 예외 발생 확인
        assertThrows(IllegalArgumentException.class, () -> {
            TimeRange.getTimeRanges(startTime, endTime, intervalHours, "ASC");
        });
    }

    @Test
    @DisplayName("오름차순 시간 범위 출력")
    void printAscTimeRange() {
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T23:59:59");
        int intervalHours = 12;

        LocalDateTime currentStart = startTime;

        while (currentStart.isBefore(endTime)) {
            LocalDateTime currentEnd = currentStart.plusHours(intervalHours).minusSeconds(1);

            if (currentEnd.isAfter(endTime)) {
                currentEnd = endTime;
            }

            System.out.println("해당 기간: " + currentStart + " to " + currentEnd);

            currentStart = currentEnd.plusSeconds(1);
        }
    }

    @Test
    @DisplayName("내림차순 시간 범위 출력")
    void printDescTimeRange() {
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T23:59:59");
        int intervalHours = 12;

        LocalDateTime currentEnd = endTime;

        while (currentEnd.isAfter(startTime)) {
            LocalDateTime currentStart = currentEnd.minusHours(intervalHours);

            // currentStart가 startTime보다 작아지면 startTime으로 설정
            if (currentStart.isBefore(startTime)) {
                currentStart = startTime;
            }

            System.out.println("해당 기간: " + currentStart + " to " + currentEnd);

            currentEnd = currentStart.minusSeconds(1);
        }
    }
}