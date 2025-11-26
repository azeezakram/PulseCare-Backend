package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;

public interface DepartmentService extends
        FindableService<String, DeptResponseDTO>,
        SavableService<DeptRequestDTO, DeptResponseDTO>,
        UpdatableService<DeptRequestDTO, DeptResponseDTO, Integer>,
        DeletableService<Integer> {
}
