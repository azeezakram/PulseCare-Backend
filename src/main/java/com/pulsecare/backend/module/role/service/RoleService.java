package com.pulsecare.backend.module.role.service;

import com.pulsecare.backend.common.base.service.*;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

public interface RoleService extends
        FindableService<Integer, RoleResDto>,
        CreatableService<RoleReqDto, RoleResDto>,
        UpdatableService<RoleReqDto, RoleResDto>,
        DeletableService<Integer, Byte> {
}
