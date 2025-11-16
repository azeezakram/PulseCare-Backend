package com.pulsecare.backend.module.user.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

public interface UserController extends
        FindableController<ResponseBody<UserResponseDTO>, String>,
        CreatableController<UserRequestDTO, ResponseBody<UserResponseDTO>>,
        UpdatableController<UserRequestDTO, ResponseBody<UserResponseDTO>>,
        DeletableController<ResponseBody<Byte>, String> {
}
