package com.royi.api.util;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeRange24 {

    public static List<Pair<LocalDateTime, LocalDateTime>> getTimeRanges(LocalDateTime startTime, LocalDateTime endTime, String order) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간 이전이어야 합니다.");
        }

        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = new ArrayList<>();
        LocalDateTime currentStart, currentEnd;

        if ("ASC".equalsIgnoreCase(order)) {
            currentStart = startTime;
            while (!currentStart.isAfter(endTime)) {
                // Set the end of the current range to the end of the day or endTime, whichever is earlier
                currentEnd = currentStart.withHour(23).withMinute(59).withSecond(59);
                if (currentEnd.isAfter(endTime)) {
                    currentEnd = endTime;
                }
                timeRanges.add(Pair.of(currentStart, currentEnd));

                // Move to the start of the next day
                currentStart = currentEnd.plusSeconds(1);
                if (currentStart.isAfter(endTime)) {
                    break;
                }
            }
        } else if ("DESC".equalsIgnoreCase(order)) {
            currentEnd = endTime;
            while (!currentEnd.isBefore(startTime)) {
                // Set the start of the current range to the start of the day or startTime, whichever is later
                currentStart = currentEnd.withHour(0).withMinute(0).withSecond(0);
                if (currentStart.isBefore(startTime)) {
                    currentStart = startTime;
                }
                timeRanges.add(Pair.of(currentStart, currentEnd));

                // Move to the end of the previous day
                currentEnd = currentStart.minusSeconds(1);
                if (currentEnd.isBefore(startTime)) {
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException("지원하지 않는 정렬: " + order);
        }

        return timeRanges;
    }
}