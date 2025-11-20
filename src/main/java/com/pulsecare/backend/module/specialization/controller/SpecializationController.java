package com.pulsecare.backend.module.specialization.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.specialization.dto.SpecializationReqDTO;
import com.pulsecare.backend.module.specialization.dto.SpecializationResDTO;

import java.util.List;

public interface SpecializationController extends
        FindableController<ResponseBody<SpecializationResDTO>, ResponseBody<List<SpecializationResDTO>>, Integer>,
        CreatableController<SpecializationReqDTO, ResponseBody<SpecializationResDTO>>,
        UpdatableController<SpecializationReqDTO, ResponseBody<SpecializationResDTO>, Integer>,
        DeletableController<ResponseBody<String>, Integer> {
}
