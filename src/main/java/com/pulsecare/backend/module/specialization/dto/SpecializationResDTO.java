package com.pulsecare.backend.module.specialization.dto;

import java.time.LocalDateTime;

public record SpecializationResDTO(
        Integer id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
