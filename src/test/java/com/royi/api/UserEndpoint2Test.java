package com.royi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.royi.api.domain.User;
import com.royi.api.dto.*;
import com.royi.api.util.TimeRange;
import com.royi.api.util.TimeRange24;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserEndpoint2Test {

    @Test
    @DisplayName("전체 수를 포함한 사용자 수 데이터를 검색")
    void getUserCounts() {
        WebClient webClient = WebClient.create("http://localhost:8082/api");

        // 요청에 사용할 객체 생성
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("이름1");
        users.add(user1);
        User user2 = new User();
        user2.setUsername("이름2");
        users.add(user2);

        RequestCriteria criteria = RequestCriteria.builder()
                .fromDate("2024-07-31T00:00:00")
                .toDate("2024-08-04T23:59:59")
                .build();

        UserCountRequest userCountRequest = UserCountRequest.builder()
                .users(users)
                .criteria(criteria)
                .build();

        Mono<UserCountResponse> responseUserDtoMono = webClient.post()
                .uri("/users/count")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userCountRequest)
                .retrieve()
                .bodyToMono(UserCountResponse.class);

        CountDownLatch latch = new CountDownLatch(1);

        responseUserDtoMono.subscribe(
                response -> {
                    // 성공적으로 데이터를 받아왔을 때
                    System.out.println("Status: " + response.isOk());
                    response.getUserCountResults().forEach(System.out::println);
                    latch.countDown(); // Notify completion
                },
                error -> {
                    System.err.println("Error: " + error.getMessage()); // 오류 처리
                    latch.countDown(); // Notify completion
                }
        );

        try {
            // 래치가 0으로 카운트다운될 때까지 최대 5초 동안 대기
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    @DisplayName("전체 수를 포함한 사용자 수 데이터를 검색 -> 사용자 리스트 검색")
//    void getUserList() {
//        WebClient webClient = WebClient.create("http://localhost:8082/api");
//        LocalDateTime searchStartTime = LocalDateTime.parse("2024-07-31T00:00:00");
//        LocalDateTime searchEndTime = LocalDateTime.parse("2024-08-04T23:59:59");
//        int intervalHours = 12;
//        int pageSize = 10;
//        int maxResults = 10;
//        String order = "ASC";
//
//        // 요청에 사용할 객체 생성
//        List<User> users = new ArrayList<>();
//        User user1 = new User();
//        user1.setUsername("이름1");
//        users.add(user1);
//        User user2 = new User();
//        user2.setUsername("이름2");
//        users.add(user2);
//
//        RequestCriteria criteria = RequestCriteria.builder()
//                .fromDate("2024-07-31T00:00:00")
//                .toDate("2024-08-04T23:59:59")
//                .build();
//
//        UserCountRequest userCountRequest = UserCountRequest.builder()
//                .users(users)
//                .criteria(criteria)
//                .build();
//
//        Mono<UserCountResponse> responseUserDtoMono = webClient.post()
//                .uri("/users/count")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(userCountRequest)
//                .retrieve()
//                .bodyToMono(UserCountResponse.class);
//
//        CountDownLatch latch = new CountDownLatch(1);
//
//        responseUserDtoMono.subscribe(initialResponse -> {
//            // Display initial response
//            System.out.println("Initial Response Status: " + initialResponse.isOk());
//            List<UserCountResult> userCountResults = initialResponse.getUserCountResults();
//
//            // Calculate the total count of existing results
//            long totalCount = userCountResults.stream()
//                    .mapToLong(UserCountResult::getCount)
//                    .sum();
//
//            List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange24.getTimeRanges(searchStartTime, searchEndTime, order);
//            // If we need more data, loop through the time ranges
//            for (Pair<LocalDateTime, LocalDateTime> timeRange : timeRanges) {
//                LocalDateTime start = timeRange.getFirst();
//                LocalDateTime end = timeRange.getSecond();
//
//                // Get counts for the specific date range
////                int remainingCount = pageSize - (int) totalCount; // Assuming total required is 10
////                if (remainingCount > 0) {
////                    UserCountRequest request = createUserCountRequestForDate(start, end, remainingCount);
////                    fetchAdditionalCounts(request);
////                    totalCount += remainingCount; // Update the total count with the newly fetched data
////                }
//            }
//        }, error -> {
//            System.err.println("Error fetching initial user counts: " + error.getMessage());
//        });
//
//        try {
//            // 래치가 0으로 카운트다운될 때까지 최대 5초 동안 대기
//            latch.await(5, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private UserCountRequest createUserCountRequestForDate(LocalDateTime start, LocalDateTime end, int limit) {
//        // Create and return the UserCountRequest for the specific date range
//        // Modify as needed based on your UserCountRequest structure
//    }
//
//    private void fetchAdditionalCounts(UserCountRequest request) {
//        // Make the API call to fetch additional user counts
//        WebClient webClient = WebClient.create("http://localhost:8082/api");
//        Mono<UserCountResponse> responseMono = webClient.post()
//                .uri("/users/count")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(UserCountResponse.class);
//
//        responseMono.subscribe(response -> {
//            // Handle the additional counts
//            System.out.println("Fetched additional counts:");
//            response.getUserCountResults().forEach(System.out::println);
//        }, error -> {
//            System.err.println("Error fetching additional user counts: " + error.getMessage());
//        });
//    }
}
