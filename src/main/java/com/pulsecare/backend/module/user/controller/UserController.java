package com.pulsecare.backend.module.user.controller;

import com.pulsecare.backend.common.base.controller.*;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

import java.util.List;

public interface UserController extends
        FindableController<ResponseBody<UserResponseDTO>, ResponseBody<List<UserResponseDTO>>, String>,
        CreatableController<UserRequestDTO, ResponseBody<UserResponseDTO>>,
        UpdatableController<UserRequestDTO, ResponseBody<UserResponseDTO>, String>,
        DeletableController<ResponseBody<String>, String> {
}
