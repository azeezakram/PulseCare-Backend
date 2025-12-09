package com.pulsecare.backend.module.resource.ward.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.ward.dto.WardReqDTO;
import com.pulsecare.backend.module.resource.ward.dto.WardResDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WardController extends
        FindableController<ResponseBody<WardResDTO>, ResponseBody<List<WardResDTO>>, Integer>,
        CreatableController<WardReqDTO, ResponseBody<WardResDTO>>,
        UpdatableController<WardReqDTO, ResponseBody<WardResDTO>, Integer>,
        DeletableController<ResponseBody<String>, Integer> {

    ResponseEntity<ResponseBody<List<WardResDTO>>> findAllByDepartmentId(Integer departmentId);
}
