package com.pulsecare.backend.module.resource.ward.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.module.resource.ward.model.Ward;

public interface WardService extends
        FindableService<Integer, Ward>,
        SavableService<Ward, Ward>,
        DeletableService<Integer> {
    void validateWardNameAndDepartmentIDUniqueness(String wardName, Integer departmentId);
}
