package com.royi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponsePaginationDto {
    private int startIndex;
    private int pageSize;
    private String order;
    private int resultCount;
}