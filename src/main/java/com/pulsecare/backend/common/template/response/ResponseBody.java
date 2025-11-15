package com.pulsecare.backend.common.template.response;

public record ResponseBody<T>(int status, String message, T data) {
}
