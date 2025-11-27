package com.pulsecare.backend.module.resource.ward.facade;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import com.pulsecare.backend.module.resource.ward.dto.WardReqDTO;
import com.pulsecare.backend.module.resource.ward.dto.WardResDTO;
import com.pulsecare.backend.module.resource.ward.maper.WardMapper;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import com.pulsecare.backend.module.resource.ward.service.WardService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WardFacade {

    private final WardService wardService;
    private final DepartmentService departmentService;
    private final WardMapper mapper;

    public WardFacade(WardService wardService, DepartmentService departmentService, @Qualifier("wardMapperImpl") WardMapper mapper) {
        this.wardService = wardService;
        this.departmentService = departmentService;
        this.mapper = mapper;
    }

    @Transactional
    public WardResDTO createWard(WardReqDTO dto) {
        if (dto.departmentId() == null) {
            throw new ValidationException("Department ID must be provided for create a ward");
        }
        wardService.validateWardNameAndDepartmentIDUniqueness(dto.name(), dto.departmentId(), null);

        Ward wardEntity = mapper.toEntity(dto);

        Department department = departmentService.findById(dto.departmentId());
        wardEntity.setDepartment(department);

        Ward savedWard = wardService.save(wardEntity);

        return mapper.toDTO(savedWard);
    }

    @Transactional
    public WardResDTO updateWard(WardReqDTO dto, Integer wardId) {
        if (dto.departmentId() == null) {
            throw new ValidationException("Department ID must be provided for updating a ward");
        }

        if (dto.name() != null && dto.name().isBlank()) {
            throw new ValidationException("Ward name must not be blank");
        }

        Ward existingWard = wardService.findWardByWardIdAndDepartmentId(wardId, dto.departmentId());

        if (dto.name() != null) {
            wardService.validateWardNameAndDepartmentIDUniqueness(dto.name(), dto.departmentId(), wardId);
        }

        mapper.updateEntity(dto, existingWard);

        Department department = departmentService.findById(dto.departmentId());
        existingWard.setDepartment(department);

        Ward updatedWard = wardService.save(existingWard);

        return mapper.toDTO(updatedWard);
    }

}
