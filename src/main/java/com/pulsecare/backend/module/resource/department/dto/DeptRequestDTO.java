package com.pulsecare.backend.module.resource.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DeptRequestDTO(
        @NotBlank(message = "Name is required")
        @NotEmpty(message = "Name is required")
        String name) {
}
