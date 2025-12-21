package com.pulsecare.backend.module.patient.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.patient.dto.PatientReqDTO;
import com.pulsecare.backend.module.patient.dto.PatientResDTO;
import com.pulsecare.backend.module.patient.model.Patient;

public interface PatientService extends
        FindableService<Long, PatientResDTO>,
        SavableService<PatientReqDTO, PatientResDTO>,
        UpdatableService<PatientReqDTO, PatientResDTO, Long>,
        DeletableService<Long> {
    Patient findEntityById(Long id);
    PatientResDTO findByNic(String nic);
}
