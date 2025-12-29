package com.pulsecare.backend.module.authentication.controller;

import com.pulsecare.backend.common.base.controller.LoggableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.authentication.dto.LoginRequestDTO;
import com.pulsecare.backend.module.authentication.dto.LoginResponseDTO;

public interface AuthenticationController extends
        LoggableController<LoginRequestDTO, ResponseBody<LoginResponseDTO>> {
}
