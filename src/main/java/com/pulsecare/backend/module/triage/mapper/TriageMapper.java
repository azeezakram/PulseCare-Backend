package com.pulsecare.backend.module.triage.mapper;

import com.pulsecare.backend.module.triage.dto.TriagePredictionReqDTO;
import com.pulsecare.backend.module.triage.dto.TriagePredictionResDTO;
import com.pulsecare.backend.module.triage.dto.TriageReqDTO;
import com.pulsecare.backend.module.triage.dto.TriageResDTO;
import com.pulsecare.backend.module.triage.model.Triage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TriageMapper {
    Triage toEntity(TriageReqDTO dto);

    @Mapping(target = "patientId", expression = "java(entity.getPatient().getId())")
    TriageResDTO toDTO(Triage entity);


    @Mapping(source = "triageLevel", target = "triageLevel")
    Triage toPredEntity(TriagePredictionResDTO dto);
    TriagePredictionReqDTO toPredDTO(Triage entity);

    TriagePredictionReqDTO toPredDTOFromReq(TriageReqDTO dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(TriageReqDTO dto, @MappingTarget Triage entity);
}
