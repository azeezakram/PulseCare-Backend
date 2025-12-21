package com.pulsecare.backend.module.patient.mapper;

import com.pulsecare.backend.module.patient.dto.PatientReqDTO;
import com.pulsecare.backend.module.patient.dto.PatientResDTO;
import com.pulsecare.backend.module.patient.model.Patient;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientResDTO toDTO(Patient entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Patient toEntity(PatientReqDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(
            PatientReqDTO dto,
            @MappingTarget Patient entity
    );

}

