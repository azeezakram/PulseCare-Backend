package com.pulsecare.backend.module.user.dto;

import com.pulsecare.backend.module.role.dto.RoleResDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(

        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String mobileNumber,
        RoleResDto role,

        String imageUrl,            // "/api/v1/user/{id}/image"

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLoginAt,
        Boolean isActive

) { }
