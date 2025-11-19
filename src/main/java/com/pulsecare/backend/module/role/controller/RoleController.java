package com.pulsecare.backend.module.role.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;

import java.util.List;

public interface RoleController extends
        FindableController<ResponseBody<RoleResDto>, ResponseBody<List<RoleResDto>>, Integer>,
        CreatableController<RoleReqDto, ResponseBody<RoleResDto>>,
        UpdatableController<RoleReqDto, ResponseBody<RoleResDto>>,
        DeletableController<ResponseBody<Byte>, Integer> {
}
