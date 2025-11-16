package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.mapper.DepartmentMapper;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.repo.DepartmentRepository;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper mapper;
    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentMapper mapper, DepartmentRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @Override
    public DeptResponseDTO findById(String id) {
        return null;
    }

    @Override
    public DeptResponseDTO findAll() {
        return null;
    }

    @Override
    public DeptResponseDTO create(DeptRequestDTO data) {
        Department department = mapper.toEntity(data);
        System.out.println(department);
        return mapper.toDTO(repository.save(department));
    }

    @Override
    public DeptResponseDTO update(DeptRequestDTO data) {
        return null;
    }

    @Override
    public Byte delete(Integer id) {
        return 0;
    }


}
