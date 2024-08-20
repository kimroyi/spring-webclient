package com.royi.api;

import com.royi.api.domain.User;
import com.royi.api.dto.RequestCriteria;
import com.royi.api.dto.RequestPagination;
import com.royi.api.dto.RequestUser;
import com.royi.api.dto.ResponseUser;
import com.royi.api.util.TimeRange;
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
public class UserEndpointTest {

    @Test
    void getUsers() {
        WebClient webClient = WebClient.create("http://localhost:8082/api");

        Mono<ResponseUser> responseMono = webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(ResponseUser.class);

        responseMono.subscribe(
                response -> {
                    // 성공적으로 데이터를 받아왔을 때
                    System.out.println("Status: " + response.isOk());
                    response.getUsers().forEach(System.out::println); // User 리스트 출력
                },
                error -> System.err.println("Error: " + error.getMessage()) // 오류 처리
        );
    }

    @Test
    void findUsersByUsername() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8082/api");

        // 요청에 사용할 객체 생성
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("이름1");
        users.add(user1);
        User user2 = new User();
        user2.setUsername("이름2");
        users.add(user2);

        RequestUser requestUser = new RequestUser();
        requestUser.setUsers(users);

        Mono<ResponseUser> responseUserDtoMono = webClient.post()
                .uri("/users/search/username")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestUser)
                .retrieve()
                .bodyToMono(ResponseUser.class);

        responseUserDtoMono.subscribe(
                responseUser -> {
                    // Handle successful response
                    System.out.println("Response User DTO: " + responseUser);
                    if (responseUser.isOk()) {
                        System.out.println("Users found: ");
                        responseUser.getUsers().forEach(System.out::println);
                    } else {
                        System.out.println("No users found or query failed.");
                    }
                },
                error -> {
                    // Handle error
                    System.err.println("Error occurred: " + error.getMessage());
                }
        );

        System.out.println("end");
    }

    @Test
    void findUsersByUserNameAndCriteria() {
        WebClient webClient = WebClient.create("http://localhost:8082/api");

        // 요청에 사용할 객체 생성
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("이름1");
        users.add(user1);
        User user2 = new User();
        user2.setUsername("이름2");
        users.add(user2);

        RequestUser requestUser = new RequestUser();
        requestUser.setUsers(users);

        requestUser.setCriteria(RequestCriteria.builder().fromDate("2024-07-31T00:00:00").toDate("2024-08-01T23:59:59").build());
        requestUser.setPagination(RequestPagination.builder().startIndex(0).pageSize(10).order("DESC").build());

        // 비동기 요청을 시작
        Mono<ResponseUser> responseUserDtoMono = webClient.post()
                .uri("/users/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestUser)
                .retrieve()
                .bodyToMono(ResponseUser.class);

        // Count가 1인 CountDownLatch 생성
        CountDownLatch latch = new CountDownLatch(1);

        // 응답이 도착하면 처리를 시작
        responseUserDtoMono.subscribe(
                responseUser -> {
                    // Handle successful response
                    System.out.println("Response User DTO: " + responseUser);
                    if (responseUser.isOk()) {
                        System.out.println("Users found: ");
                        responseUser.getUsers().forEach(System.out::println);
                    } else {
                        System.out.println("No users found or query failed.");
                    }
                    latch.countDown(); // Notify completion
                },
                error -> {
                    // Handle error
                    System.err.println("Error occurred: " + error.getMessage());
                    latch.countDown(); // Notify completion
                }
        );

        System.out.println("test");

        /**
         * CountDownLatch 사용이유
         * subscribe 콜백이 실행되기 전에 테스트가 완료되는 이슈
         * subscribe 메소드가 테스트 스레드를 차단하지 않기 때문에 발생
         */
        try {
            // 래치가 0으로 카운트다운될 때까지 최대 5초 동안 대기
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end");
    }

    @Test
    void findUsersByUserNameAndCriteria_page() {
        WebClient webClient = WebClient.create("http://localhost:8082/api");

        LocalDateTime searchStartTime = LocalDateTime.parse("2024-07-31T00:00:00");
        LocalDateTime searchEndTime = LocalDateTime.parse("2024-08-04T23:59:59");
        int intervalHours = 12;
        int pageSize = 10;
        int maxResults = 10;
        String order = "ASC";

        List<Pair<LocalDateTime, LocalDateTime>> timeRanges = TimeRange.getTimeRanges(searchStartTime, searchEndTime, intervalHours, order);

        List<User> allUsers = new ArrayList<>();
        LocalDateTime currentStart = searchStartTime;
        AtomicInteger startIndex = new AtomicInteger(0); // Use AtomicInteger to track the startIndex

        for (Pair<LocalDateTime, LocalDateTime> timeRange : timeRanges) {
            LocalDateTime startTime = timeRange.getFirst();
            currentStart = startTime;

            LocalDateTime endTime = timeRange.getSecond();

            RequestUser requestUser = new RequestUser();
            requestUser.setUsers(createUserList()); // Implement createUserList() if needed
            requestUser.setCriteria(RequestCriteria.builder()
                    .fromDate(startTime.toString())
                    .toDate(endTime.toString())
                    .build());
            requestUser.setPagination(RequestPagination.builder()
                    .startIndex(startIndex.get()) // Pass the current startIndex
                    .pageSize(pageSize)
                    .order(order)
                    .build());

            // Create a new CountDownLatch for each request
            CountDownLatch latch = new CountDownLatch(1);

            // Fetch data and accumulate results
            Mono<ResponseUser> responseUserDtoMono = webClient.post()
                    .uri("/users/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestUser)
                    .retrieve()
                    .bodyToMono(ResponseUser.class);

            responseUserDtoMono.subscribe(
                    responseUser -> {
                        if (responseUser.isOk()) {
                            System.out.println("해당 기간의 검색된 사용자 있음: " + startTime + " to " + endTime);
                            System.out.println("검색된 사용자 수 : " + responseUser.getUsers().size());
                            System.out.println("검색된 사용자 정보: " + responseUser.getUsers());

                            // Calculate how many users can still be added
                            int remainingSlots = maxResults - allUsers.size();

                            // Add only the remaining slots, if not enough slots, continue from the last added user
                            int usersToAdd = Math.min(responseUser.getUsers().size(), remainingSlots);
                            allUsers.addAll(responseUser.getUsers().subList(0, usersToAdd));

                            // Update the startIndex to the position where you stopped
                            startIndex.addAndGet(usersToAdd+1);
                        } else {
                            System.out.println("해당 기간의 검색된 사용자 없음: " + startTime + " to " + endTime);
                        }
                        latch.countDown(); // Notify completion
                    },
                    error -> {
                        System.err.println("Error occurred: " + error.getMessage());
                        latch.countDown(); // Notify completion
                    }
            );

            try {
                latch.await(5, TimeUnit.SECONDS); // Wait for the response
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If we've reached the maximum number of results, exit the loop
            if (allUsers.size() >= maxResults) {
                break;
            }

            // Reset startIndex to 0 for the next time period if you've finished adding users from the previous period
            startIndex.set(0);
        }

        System.out.println("총 가져온 사용자 수 : " + allUsers.size());
        allUsers.forEach(System.out::println);

        // If needed, you can use the last startIndex and period for subsequent pages
        System.out.println("다음 페이지를 시작할 startIndex: " + startIndex.get());
        System.out.println("다음 페이지를 시작할 마지막 검색 기간: " + currentStart);
    }

    private List<User> createUserList() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("이름1");
        users.add(user1);
        User user2 = new User();
        user2.setUsername("이름2");
        users.add(user2);
        // Add logic to create or fetch users as needed
        return users;
    }
}
