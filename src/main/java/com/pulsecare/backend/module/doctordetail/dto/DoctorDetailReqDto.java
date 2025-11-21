package com.pulsecare.backend.module.doctordetail.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DoctorDetailReqDto(
        @NotEmpty(message = "License No is required")
        String licenseNo,
        @NotEmpty(message = "User ID is required")
        String userId,
        @NotEmpty(message = "Specialization IDs are required")
        List<Integer> specializationIds
) {
}
