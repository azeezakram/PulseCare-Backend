package com.pulsecare.backend.module.doctordetail.dto;

import java.util.Set;

public record DoctorDetailReqDto(
        String licenseNo,
        String userId,
        Set<Integer> specializationIds
) {
}
