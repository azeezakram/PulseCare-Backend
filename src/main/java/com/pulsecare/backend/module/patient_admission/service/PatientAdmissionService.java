package com.pulsecare.backend.module.patient_admission.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionReqDTO;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionResDTO;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;

public interface PatientAdmissionService extends
        FindableService<Long, PatientAdmissionResDTO>,
        SavableService<PatientAdmissionReqDTO, PatientAdmissionResDTO>,
        UpdatableService<PatientAdmissionReqDTO, PatientAdmissionResDTO, Long>,
        DeletableService<Long> {
    PatientAdmission findEntityById(Long id);
    Boolean hasActiveAdmission(Long id);
}
