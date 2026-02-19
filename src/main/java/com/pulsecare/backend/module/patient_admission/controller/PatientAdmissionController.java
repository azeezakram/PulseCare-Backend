package com.pulsecare.backend.module.patient_admission.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionReqDTO;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionResDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientAdmissionController extends
        FindableController<ResponseBody<PatientAdmissionResDTO>, ResponseBody<List<PatientAdmissionResDTO>>, Long>,
        CreatableController<PatientAdmissionReqDTO, ResponseBody<PatientAdmissionResDTO>>,
        UpdatableController<PatientAdmissionReqDTO, ResponseBody<PatientAdmissionResDTO>, Long>,
        DeletableController<ResponseBody<String>, Long> {
    ResponseEntity<ResponseBody<Boolean>> hasActiveAdmission(Long id);
}
