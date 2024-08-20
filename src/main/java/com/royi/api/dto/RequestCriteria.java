package com.royi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestCriteria {
    private String fromDate;
    private String toDate;
}