package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponseDTO create(UserRequestDTO data) {
        return null;
    }

    @Override
    public String delete(Byte id) {
        return "";
    }

    @Override
    public UserResponseDTO findById(String id) {
        return null;
    }

    @Override
    public UserResponseDTO findAll() {
        return null;
    }

    @Override
    public UserResponseDTO update(UserRequestDTO data) {
        return null;
    }
}
