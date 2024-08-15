package com.royi.api.service;

import com.royi.api.domain.User;
import com.royi.api.dto.RequestUserDto;
import com.royi.api.dto.ResponseUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("사용자검색 서비스 테스트")
    void findUsers() {
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
        ResponseUserDto users1 = userService.findUsers(requestUserDto);
        System.out.println(users1);
    }
}