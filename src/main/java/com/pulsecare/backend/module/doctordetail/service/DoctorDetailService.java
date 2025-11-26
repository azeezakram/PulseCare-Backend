package com.pulsecare.backend.module.doctordetail.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;

import java.util.UUID;

public interface DoctorDetailService extends
        FindableService<Long, DoctorDetail>,
        SavableService<DoctorDetail, DoctorDetail>,
        DeletableService<Long> {
    DoctorDetail findByUserId(String userId);
    void validateAlreadyHasDoctorDetail(UUID userId);
    void validateLicenseNoUniqueness(String licenseNo, UUID userId);
}
