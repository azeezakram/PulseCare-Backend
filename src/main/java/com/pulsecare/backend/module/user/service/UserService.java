package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.common.base.service.*;
import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.model.Users;

public interface UserService extends
        FindableService<String, Users>,
        CreatableService<Users, Users>,
        UpdatableService<Users, Users, String>,
        DeletableService<String>,
        LoggableService<LoginRequestDTO, String> {
}
