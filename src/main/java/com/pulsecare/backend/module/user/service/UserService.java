package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.common.base.service.*;
import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

public interface UserService extends
        FindableService<String, UserResponseDTO>,
        CreatableService<UserRequestDTO, UserResponseDTO>,
        UpdatableService<UserRequestDTO, UserResponseDTO>,
        DeletableService<String, Byte>,
        LoggableService<LoginRequestDTO, String> {
}
