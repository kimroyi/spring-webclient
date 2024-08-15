package com.royi.api.service;

import com.royi.api.domain.User;
import com.royi.api.dto.RequestCriteriaDto;
import com.royi.api.dto.RequestPaginationDto;
import com.royi.api.dto.RequestUserDto;
import com.royi.api.dto.ResponseUserDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @DisplayName("사용자이름검색 서비스 테스트")
    void findUsersByUsername() {
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

        ResponseUserDto users1 = userService.findUsersByUsername(requestUserDto);
        System.out.println(users1);
    }

    @Test
    @Order(2)
    @DisplayName("사용자이름&일자검색 서비스 테스트")
    void findUsersByUserNameAndCriteria() {
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
        requestUserDto.setCriteria(RequestCriteriaDto.builder().fromDate("2024-07-31T00:00:00").toDate("2024-08-01T23:59:59").build());
        requestUserDto.setPagination(RequestPaginationDto.builder().startIndex(0).pageSize(10).order("DESC").build());

        ResponseUserDto users1 = userService.findUsersByUserNameAndCriteria(requestUserDto);
        System.out.println(users1);
    }
}