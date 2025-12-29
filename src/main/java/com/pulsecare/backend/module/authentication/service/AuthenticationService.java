package com.pulsecare.backend.module.authentication.service;

import com.pulsecare.backend.common.base.service.LoggableService;
import com.pulsecare.backend.module.authentication.dto.LoginRequestDTO;
import com.pulsecare.backend.module.authentication.dto.LoginResponseDTO;

public interface AuthenticationService extends
        LoggableService<LoginRequestDTO, LoginResponseDTO> {
}
