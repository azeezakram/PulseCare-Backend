package com.pulsecare.backend.module.user.facade;

import com.pulsecare.backend.module.role.model.Role;
import com.pulsecare.backend.module.role.service.RoleService;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.mapper.UserMapper;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserFacade {
    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserFacade(UserService userService, RoleService roleService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

@Transactional
public UserResponseDTO createNewUser(UserRequestDTO data) {
    userService.validateUsernameDoesNotExist(data.username());

    Users userEntity = userMapper.toEntity(data);

    if (data.roles() != null && !data.roles().isEmpty()) {
        Set<Role> roles = roleService.findAllById(data.roles());
        userEntity.setRoles(roles);
    }

    if (data.password() != null && !data.password().isEmpty()) {
        userEntity.setPassword(passwordEncoder.encode(data.password()));
    }

    Users savedUser = userService.save(userEntity);

    return userMapper.toDTO(savedUser);
}

    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO data, String id) {
        Users existingUser = userService.findById(id);

        if (data.username() != null) {
            userService.validateUsernameUniqueness(data.username(), existingUser.getId());
        }

        userMapper.updateEntity(data, existingUser);

        if (data.roles() != null && !data.roles().isEmpty()) {
            Set<Role> roles = roleService.findAllById(data.roles());
            existingUser.setRoles(roles);
        }

        if (data.password() != null && !data.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(data.password()));
        }

        Users updatedUser = userService.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

}
