package com.pulsecare.backend.module.doctor_detail.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.module.doctor_detail.model.DoctorDetail;

import java.util.UUID;

public interface DoctorDetailService extends
        FindableService<Long, DoctorDetail>,
        SavableService<DoctorDetail, DoctorDetail>,
        DeletableService<Long> {
    DoctorDetail findByUserId(String userId);
    void validateAlreadyHasDoctorDetail(UUID userId);
    void validateLicenseNoUniqueness(String licenseNo, UUID userId);
}
