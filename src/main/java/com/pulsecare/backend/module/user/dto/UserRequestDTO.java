package com.pulsecare.backend.module.user.dto;

import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailReqDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserRequestDTO(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Username is required")
        String username,

        String email,

        @NotBlank(message = "Password is required")
        String password,

        String mobileNumber,

        @NotNull(message = "Roles must be provided")
        Set<Integer> roles,

        Boolean isActive,

        DoctorDetailReqDto doctorDetails
) {
}
