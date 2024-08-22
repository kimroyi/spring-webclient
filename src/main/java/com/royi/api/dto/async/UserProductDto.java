package com.royi.api.dto.async;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserProductDto {
    private String userId;
    private String usernm;
    private ProductDto product;
}