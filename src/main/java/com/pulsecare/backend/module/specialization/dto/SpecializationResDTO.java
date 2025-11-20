package com.pulsecare.backend.module.specialization.dto;

import com.pulsecare.backend.module.role.model.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record SpecializationResDTO(

        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String mobileNumber,

        Set<Role> roles,          // ROLE_ADMIN, ROLE_DOCTOR, ROLE_NURSE

        String imageUrl,            // "/api/v1/user/{id}/image"

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLoginAt,
        Boolean isActive

) { }
