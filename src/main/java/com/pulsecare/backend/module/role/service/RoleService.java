package com.pulsecare.backend.module.role.service;

import com.pulsecare.backend.common.base.service.CreatableService;
import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;

public interface RoleService extends
        FindableService<Integer, RoleResDto>,
        CreatableService<RoleReqDto, RoleResDto>,
        UpdatableService<RoleReqDto, RoleResDto, Integer>,
        DeletableService<Integer> {
}
