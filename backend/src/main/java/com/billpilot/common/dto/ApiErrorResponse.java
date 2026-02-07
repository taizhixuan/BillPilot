package com.billpilot.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private final int status;
    private final String message;
    private final String path;
    private final Map<String, String> errors;
    private final Instant timestamp = Instant.now();

    public ApiErrorResponse(int status, String message, String path, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }
}
