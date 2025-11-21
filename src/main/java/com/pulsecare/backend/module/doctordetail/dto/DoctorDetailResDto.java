package com.pulsecare.backend.module.doctordetail.dto;

import com.pulsecare.backend.module.specialization.dto.SpecializationResDTO;
import java.util.List;

public record DoctorDetailResDto(
        Long id,
        String licenseNo,
        String userId,
        List<SpecializationResDTO> specializations
) {
}
