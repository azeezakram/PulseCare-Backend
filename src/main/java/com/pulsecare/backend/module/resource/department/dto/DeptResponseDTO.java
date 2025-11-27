package com.pulsecare.backend.module.resource.department.dto;

import java.time.LocalDateTime;

public record DeptResponseDTO(
        Integer id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
