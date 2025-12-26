package com.pulsecare.backend.module.prescription.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import com.pulsecare.backend.module.prescription.dto.PrescriptionDetailResDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionReqDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionSummaryResDTO;
import com.pulsecare.backend.module.prescription.model.Prescription;

public interface PrescriptionService extends
        FindableService<Long, PrescriptionSummaryResDTO>,
        SavableService<PrescriptionReqDTO, PrescriptionDetailResDTO>,
        UpdatableService<PrescriptionReqDTO, PrescriptionDetailResDTO, Long>,
        DeletableService<Long> {
    PrescriptionDetailResDTO findWithDetailById(Long id);
    Prescription findEntityById(Long id);
}
