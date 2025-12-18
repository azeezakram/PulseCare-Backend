package com.pulsecare.backend.module.patient_queue.mapper;

import com.pulsecare.backend.module.patient_queue.dto.PatientQueueReqDTO;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueResDTO;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientQueueMapper {

    @Mapping(source = "triage.id", target = "triageId")
    @Mapping(source = "triage.triageLevel", target = "triageLevel")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "priority", target = "priority")
    PatientQueueResDTO toDTO(PatientQueue entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "triage", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "admitted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PatientQueue toEntity(PatientQueueReqDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "admitted", ignore = true)
    @Mapping(target = "triage", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(
            PatientQueueReqDTO dto,
            @MappingTarget PatientQueue entity
    );
}

