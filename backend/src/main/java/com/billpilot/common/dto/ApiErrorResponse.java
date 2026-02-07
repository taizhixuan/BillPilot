package com.billpilot.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private final int status;
    private final String message;
    private final String path;
    private final Map<String, String> errors;
    private final Instant timestamp = Instant.now();
}
