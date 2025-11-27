package com.pulsecare.backend.module.resource.department.facade;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.mapper.DepartmentMapper;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentFacade {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    public DepartmentFacade(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @Transactional
    public DeptResponseDTO createDepartment(DeptRequestDTO dto) {
        departmentService.validateNameDoesNotExist(dto.name());
        Department savedDepartment = departmentService.save(departmentMapper.toEntity(dto));
        return departmentMapper.toDTO(savedDepartment);
    }

    @Transactional
    public DeptResponseDTO updateDepartment(DeptRequestDTO dto, Integer departmentId) {
        Department existingDepartment = departmentService.findById(departmentId);

        if (dto.name() != null && dto.name().isBlank()) {
            throw new ValidationException("Department name must not be blank");
        }


        if (dto.name() != null) {
            departmentService.validateNameUniqueness(dto.name(), departmentId);
            existingDepartment.setName(dto.name());
        }

        Department updatedDepartment = departmentService.save(existingDepartment);
        return departmentMapper.toDTO(updatedDepartment);
    }

}
