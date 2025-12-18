package com.pulsecare.backend.module.doctor_detail.dto;

import java.util.Set;

public record DoctorDetailReqDto(
        String licenseNo,
        String userId,
        Set<Integer> specializationIds
) {
}
