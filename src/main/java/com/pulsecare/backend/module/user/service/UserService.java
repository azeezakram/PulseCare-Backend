package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.common.base.service.*;
import com.pulsecare.backend.module.user.dto.UserImageProjection;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.model.Users;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface UserService extends
        FindableService<String, Users>,
        SavableService<Users, Users>,
        DeletableService<String> {
    UserResponseDTO findByUsername(String username);
    UserImageProjection getUserProfileImage(UUID userId);
    void saveUserProfileImage(UUID userId, MultipartFile image) throws IOException;
    void validateUsernameUniqueness(String newUsername, UUID currentUserId);
    void validateUsernameDoesNotExist(String username);
}
