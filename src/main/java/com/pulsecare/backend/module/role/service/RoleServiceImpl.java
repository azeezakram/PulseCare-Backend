package com.pulsecare.backend.module.role.service;

import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {


    @Override
    public RoleResDto create(RoleReqDto data) {
        return null;
    }

    @Override
    public Byte delete(Integer id) {
        return 0;
    }

    @Override
    public RoleResDto findById(Integer id) {
        return null;
    }

    @Override
    public List<RoleResDto> findAll() {
        return List.of();
    }

    @Override
    public RoleResDto update(RoleReqDto data) {
        return null;
    }
}
