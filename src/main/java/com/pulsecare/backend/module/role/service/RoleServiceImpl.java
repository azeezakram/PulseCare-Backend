package com.pulsecare.backend.module.role.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.role.mapper.RoleMapper;
import com.pulsecare.backend.module.role.model.Role;
import com.pulsecare.backend.module.role.repository.RoleRepository;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RoleResDto findById(Integer id) {
        Role data = repository.findById(id).orElse(null);

        return Optional.ofNullable(data)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public List<RoleResDto> findAll() {
        return List.of();
    }

    @Override
    public RoleResDto create(RoleReqDto data) {
        return null;
    }

    @Override
    public RoleResDto update(Integer integer, RoleReqDto data) {
        return null;
    }
    @Override
    public void delete(Integer id) {

    }
}
