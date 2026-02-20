package com.pulsecare.backend.module.prescription.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.prescription.dto.PrescriptionDetailResDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionReqDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionSummaryResDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrescriptionController extends
        FindableController<ResponseBody<PrescriptionSummaryResDTO>, ResponseBody<List<PrescriptionSummaryResDTO>>, Long>,
        CreatableController<PrescriptionReqDTO, ResponseBody<PrescriptionDetailResDTO>>,
        UpdatableController<PrescriptionReqDTO, ResponseBody<PrescriptionDetailResDTO>, Long>,
        DeletableController<ResponseBody<String>, Long> {
    ResponseEntity<ResponseBody<PrescriptionDetailResDTO>> findWithDetailById(Long id);
    ResponseEntity<ResponseBody<List<PrescriptionDetailResDTO>>> findAllByAdmissionsId(Long id);
}
