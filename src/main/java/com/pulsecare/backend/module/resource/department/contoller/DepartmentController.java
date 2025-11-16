package com.pulsecare.backend.module.resource.department.contoller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

public interface DepartmentController extends
        FindableController<ResponseBody<DeptResponseDTO>, Integer>,
        CreatableController<DeptRequestDTO, ResponseBody<DeptResponseDTO>>,
        UpdatableController<DeptRequestDTO, ResponseBody<DeptResponseDTO>>,
        DeletableController<ResponseBody<Byte>, Integer> {
}
