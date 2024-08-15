package com.royi.api.dto;

import com.royi.api.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestUserDto {
    private List<User> users;
}
