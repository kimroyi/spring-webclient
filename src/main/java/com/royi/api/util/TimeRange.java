package com.royi.api.util;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeRange {

    public static List<Pair<LocalDateTime, LocalDateTime>> getTimeRanges(LocalDateTime startTime, LocalDateTime endTime, int intervalHours, String order) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간 이전이어야 합니다.");
        }

        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = new ArrayList<>();
        LocalDateTime currentStart, currentEnd;

        if ("ASC".equalsIgnoreCase(order)) {
            currentStart = startTime;
            while (currentStart.isBefore(endTime)) {
                currentEnd = currentStart.plusHours(intervalHours).minusSeconds(1);
                if (currentEnd.isAfter(endTime)) {
                    currentEnd = endTime;
                }
                timeRanges.add(Pair.of(currentStart, currentEnd));
                currentStart = currentEnd.plusSeconds(1);
            }
        } else if ("DESC".equalsIgnoreCase(order)) {
            currentEnd = endTime;
            while (currentEnd.isAfter(startTime)) {
                currentStart = currentEnd.minusHours(intervalHours);
                if (currentStart.isBefore(startTime)) {
                    currentStart = startTime;
                }
                timeRanges.add(Pair.of(currentStart, currentEnd));
                currentEnd = currentStart.minusSeconds(1);
            }
        } else {
            throw new IllegalArgumentException("지원하지 않는 정렬: " + order);
        }

        return timeRanges;
    }
}
