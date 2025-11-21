package com.pulsecare.backend.module.role.dto;

import jakarta.validation.constraints.NotEmpty;

public record RoleReqDto(
        @NotEmpty(message = "Name is required")
        String name
) {
}
