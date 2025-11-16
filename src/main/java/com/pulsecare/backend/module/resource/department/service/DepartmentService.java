package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.common.base.service.CreatableService;
import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

public interface DepartmentService extends
        FindableService<String, DeptResponseDTO>,
        CreatableService<DeptRequestDTO, DeptResponseDTO>,
        UpdatableService<DeptRequestDTO, DeptResponseDTO>,
        DeletableService<Integer, Byte> {
}
