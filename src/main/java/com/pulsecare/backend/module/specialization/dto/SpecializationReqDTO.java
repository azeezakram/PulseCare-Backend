package com.pulsecare.backend.module.specialization.dto;

import jakarta.validation.constraints.NotBlank;

public record SpecializationReqDTO(
        @NotBlank(message = "Specialization name is required")
        String name
) { }
