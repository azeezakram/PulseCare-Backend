package com.pulsecare.backend.module.user.admin.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.user.admin.dto.AdminRequestDTO;
import com.pulsecare.backend.module.user.admin.dto.AdminResponseDTO;

public interface AdminController extends FindableController<ResponseBody<AdminResponseDTO>, String>,
        CreatableController<AdminRequestDTO, ResponseBody<AdminResponseDTO>>,
        UpdatableController<AdminRequestDTO, ResponseBody<AdminResponseDTO>>,
        DeletableController<ResponseBody<AdminResponseDTO>, String> {
}
