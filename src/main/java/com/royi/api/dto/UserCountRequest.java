package com.royi.api.dto;

import com.royi.api.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserCountRequest {
    private List<User> users;
    private RequestCriteria criteria;
}
