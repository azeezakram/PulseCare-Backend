package com.pulsecare.backend.module.triage.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.triage.dto.TriageReqDTO;
import com.pulsecare.backend.module.triage.dto.TriageResDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TriageController extends
        FindableController<ResponseBody<TriageResDTO>, ResponseBody<List<TriageResDTO>>, Long>,
        CreatableController<TriageReqDTO, ResponseBody<TriageResDTO>>,
        UpdatableController<TriageReqDTO, ResponseBody<TriageResDTO>, Long>,
        DeletableController<ResponseBody<String>, Long> {
    ResponseEntity<ResponseBody<TriageResDTO>> predict(@Valid TriageReqDTO dto);
}
