package com.pulsecare.backend.module.authentication.dto;

import java.util.Set;

public record LoginResponseDTO(
        String token,
        String username,
        Set<String> roles
) {}

