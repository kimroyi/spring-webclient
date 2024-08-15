package com.royi.api.dto;

import com.royi.api.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ResponseUserDto {
    private boolean ok;
    private List<User> users;
}
