package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.common.base.service.*;
import com.pulsecare.backend.module.user.model.Users;

import java.util.UUID;

public interface UserService extends
        FindableService<String, Users>,
        SavableService<Users, Users>,
        DeletableService<String> {
    void validateUsernameUniqueness(String newUsername, UUID currentUserId);
    void validateUsernameDoesNotExist(String username);
}
