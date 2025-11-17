package com.pulsecare.backend.module.user.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(

        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String mobileNumber,

        Set<String> roles,          // ROLE_ADMIN, ROLE_DOCTOR, ROLE_NURSE

        String imageUrl,            // "/api/v1/user/{id}/image"

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isActive

) { }
