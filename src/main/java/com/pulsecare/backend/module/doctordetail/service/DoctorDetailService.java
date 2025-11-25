package com.pulsecare.backend.module.doctordetail.service;

import com.pulsecare.backend.common.base.service.CreatableService;
import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;

public interface DoctorDetailService extends
        FindableService<Long, DoctorDetail>,
        CreatableService<DoctorDetail, DoctorDetail>,
        UpdatableService<DoctorDetail, DoctorDetail, Long>,
        DeletableService<Long> {
    DoctorDetail update(String userID, DoctorDetail data);
    DoctorDetail findByUserId(String userId);
}
