package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.module.resource.department.model.Department;

public interface DepartmentService extends
        FindableService<Integer, Department>,
        SavableService<Department, Department>,
        DeletableService<Integer> {
    void validateNameUniqueness(String departmentName, Integer departmentId);
    void validateNameDoesNotExist(String departmentName);
}
