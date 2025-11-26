package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.mapper.DepartmentMapper;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<DeptResponseDTO> findAll() {
        List<Department> departments = repository.findAll();

        if (departments.isEmpty()) {
            return List.of();
        }

        return departments.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public DeptResponseDTO save(DeptRequestDTO data) {
        Department department = mapper.toEntity(data);
        return mapper.toDTO(repository.save(department));
    }

    @Override
    public void delete(Integer id) {
        // TODO document why this method is empty
    }

    @Override
    public DeptResponseDTO update(Integer integer, DeptRequestDTO data) {
        return null;
    }
}
