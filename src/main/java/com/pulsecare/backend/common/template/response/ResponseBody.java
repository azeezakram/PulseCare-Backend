package com.pulsecare.backend.common.template.response;

import java.time.LocalDateTime;

public record ResponseBody<T>(int status, String message, T data, LocalDateTime timestamp) {
    public ResponseBody(int status, String message, T data) {
        this(status, message, data, LocalDateTime.now());
    }
}

