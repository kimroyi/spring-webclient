package com.royi.api.controller;

import com.royi.api.dto.RequestUser;
import com.royi.api.dto.ResponseUser;
import com.royi.api.dto.UserCountRequest;
import com.royi.api.dto.UserCountResponse;
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
    public ResponseUser getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users/search/username")
    public ResponseUser findUsersByUsername(@RequestBody RequestUser requestUser) {
        return userService.findUsersByUsername(requestUser);
    }

    @PostMapping("/users/search")
    public ResponseUser findUsersByUserNameAndCriteria(@RequestBody RequestUser requestUser) {
        return userService.findUsersByUserNameAndCriteria(requestUser);
    }

    @PostMapping("/users/count")
    public UserCountResponse getUserCounts(@RequestBody UserCountRequest userCountRequest) {
        return userService.getUserCountsWithTotal(userCountRequest);
    }
}
