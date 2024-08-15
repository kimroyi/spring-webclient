package com.royi.api;

import com.royi.api.domain.User;
import com.royi.api.dto.RequestUserDto;
import com.royi.api.dto.ResponseUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserEndpointTest {

    @Test
    void getUsers() {
        WebClient webClient = WebClient.create("http://localhost:8082/api");

        Mono<ResponseUserDto> responseMono = webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(ResponseUserDto.class);

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
    void findUsers() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8082/api");

        // 요청에 사용할 객체 생성
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("이름1");
        users.add(user1);
        User user2 = new User();
        user2.setUsername("이름2");
        users.add(user2);

        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setUsers(users);

        Mono<ResponseUserDto> responseUserDtoMono = webClient.post()
                .uri("/users/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestUserDto)
                .retrieve()
                .bodyToMono(ResponseUserDto.class);

        responseUserDtoMono.subscribe(
                responseUserDto -> {
                    // Handle successful response
                    System.out.println("Response User DTO: " + responseUserDto);
                    if (responseUserDto.isOk()) {
                        System.out.println("Users found: ");
                        responseUserDto.getUsers().forEach(System.out::println);
                    } else {
                        System.out.println("No users found or query failed.");
                    }
                },
                error -> {
                    // Handle error
                    System.err.println("Error occurred: " + error.getMessage());
                }
        );

        System.out.println("여기느?");
    }
}
