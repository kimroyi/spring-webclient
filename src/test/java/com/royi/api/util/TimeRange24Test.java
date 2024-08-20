package com.royi.api.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 시간 하루 범위 조회 테스트
 */
class TimeRange24Test {
    @Test
    void getTimeRanges() {
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:11:10");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T22:22:59");

        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange24.getTimeRanges(startTime, endTime, "DESC");

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
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:11:10");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T22:22:59");
        int intervalHours = 12;

        // When
        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange24.getTimeRanges(startTime, endTime, "ASC");

        // Then
        assertThat(timeRanges.size()).isEqualTo(5);
        assertThat(timeRanges.get(0).getFirst().toString()).isEqualTo(startTime.toString());
    }

    @Test
    @DisplayName("내림차순 테스트 케이스")
    void getTimeRanges_desc_normal() {
        // Given
        LocalDateTime startTime = LocalDateTime.parse("2024-07-31T00:11:10");
        LocalDateTime endTime = LocalDateTime.parse("2024-08-04T22:22:59");
        int intervalHours = 12;

        // When
        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange24.getTimeRanges(startTime, endTime, "DESC");

        // Then
        assertThat(timeRanges.size()).isEqualTo(5);
        assertThat(timeRanges.get(0).getSecond().toString()).isEqualTo(endTime.toString());
    }

    @Test
    @DisplayName("파라미터 예외 테스트 케이스")
    void getTimeRanges_invalidOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            TimeRange24.getTimeRanges(LocalDateTime.now(), LocalDateTime.now().plusDays(1), "INVALID");
        });
    }

    @Test
    @DisplayName("시작 시간이 끝 시간보다 큰 경우 테스트 케이스")
    void getTimeRanges_startTimeAfterEndTime() {
        LocalDateTime startTime = LocalDateTime.parse("2024-08-04T22:22:59");
        LocalDateTime endTime = LocalDateTime.parse("2024-07-31T00:11:10");

        // 예외 발생 확인
        assertThrows(IllegalArgumentException.class, () -> {
            TimeRange24.getTimeRanges(startTime, endTime, "ASC");
        });
    }
}