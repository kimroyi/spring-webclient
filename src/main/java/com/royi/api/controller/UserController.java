package com.royi.api.controller;

import com.royi.api.dto.RequestUserDto;
import com.royi.api.dto.ResponseUserDto;
import com.royi.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseUserDto getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users/search/username")
    public ResponseUserDto findUsersByUsername(@RequestBody RequestUserDto requestUserDto) {
        return userService.findUsersByUsername(requestUserDto);
    }

    @PostMapping("/users/search")
    public ResponseUserDto findUsersByUserNameAndCriteria(@RequestBody RequestUserDto requestUserDto) {
        return userService.findUsersByUserNameAndCriteria(requestUserDto);
    }
}
