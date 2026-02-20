package com.pulsecare.backend.module.role.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.role.mapper.RoleMapper;
import com.pulsecare.backend.module.role.model.Role;
import com.pulsecare.backend.module.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    public RoleServiceImpl(RoleRepository repository, @Qualifier("roleMapperImpl") RoleMapper mapper) {
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
    public Role findEntityById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public List<RoleResDto> findAll() {
        List<Role> data = repository.findAll();
        return data.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public RoleResDto save(RoleReqDto data) {
        repository.findByName(data.name())
                .ifPresent(s -> {
                    throw new ResourceAlreadyExistsException("Role with this name already exists");
                });
        Role entity = repository.save(mapper.toEntity(data));
        return mapper.toDTO(entity);
    }

    @Override
    public RoleResDto update(Integer id, RoleReqDto data) {
        Role existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " not found"));

        existing.setName(data.name());
        Role updated = repository.save(existing);
        return mapper.toDTO(updated);
    }

    @Override
    public void delete(Integer id) {
        Role entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        repository.delete(entity);
    }


    @Override
    public Set<Role> findAllById(Set<Integer> ids) {
        List<Role> data = repository.findAllById(ids);
        if (data.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more roles not found");
        }
        return new HashSet<>(data);
    }
}
