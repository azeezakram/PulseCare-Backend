package com.pulsecare.backend.module.authentication.dto;

public record LoginResponseDTO(
        String token,
        String username,
        String role
) {}

