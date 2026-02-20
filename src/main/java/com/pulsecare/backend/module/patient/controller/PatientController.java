package com.pulsecare.backend.module.patient.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.patient.dto.PatientReqDTO;
import com.pulsecare.backend.module.patient.dto.PatientResDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientController extends
        FindableController<ResponseBody<PatientResDTO>, ResponseBody<List<PatientResDTO>>, Long>,
        CreatableController<PatientReqDTO, ResponseBody<PatientResDTO>>,
        UpdatableController<PatientReqDTO, ResponseBody<PatientResDTO>, Long>,
        DeletableController<ResponseBody<String>, Long> {
    ResponseEntity<ResponseBody<PatientResDTO>> findByNic(String nic);
    ResponseEntity<ResponseBody<List<PatientResDTO>>> findAllAndActive();
    ResponseEntity<ResponseBody<PatientResDTO>> findByIdAndActive(Long id);
    ResponseEntity<ResponseBody<PatientResDTO>> findByNicAndActive(String nic);
}
