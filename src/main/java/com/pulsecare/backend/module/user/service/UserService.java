package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.common.base.service.CreatableService;
import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

public interface UserService extends
        FindableService<String, UserResponseDTO>,
        CreatableService<UserRequestDTO, UserResponseDTO>,
        UpdatableService<UserRequestDTO, UserResponseDTO>,
        DeletableService<Byte, String> {
}
