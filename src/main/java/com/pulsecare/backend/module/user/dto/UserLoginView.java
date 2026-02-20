package com.pulsecare.backend.module.user.dto;

import com.pulsecare.backend.module.role.model.Role;

import java.util.UUID;

public record UserLoginView(
        UUID id,
        String username,
        String password,
        String firstName,
        String lastName,
        Boolean isActive,
        Role role
) {
}
