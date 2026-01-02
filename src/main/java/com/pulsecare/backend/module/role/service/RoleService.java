package com.pulsecare.backend.module.role.service;

import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.role.model.Role;

import java.util.Set;

public interface RoleService extends
        FindableService<Integer, RoleResDto>,
        SavableService<RoleReqDto, RoleResDto>,
        UpdatableService<RoleReqDto, RoleResDto, Integer>,
        DeletableService<Integer> {
    Set<Role> findAllById(Set<Integer> ids);
    Role findEntityById(Integer id);
}
