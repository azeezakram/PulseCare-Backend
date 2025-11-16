package com.pulsecare.backend.module.resource.department.mapper;

import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toEntity(DeptRequestDTO dto);
    DeptResponseDTO toDTO(Department obj);
}
