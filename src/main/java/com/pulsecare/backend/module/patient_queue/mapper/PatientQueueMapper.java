package com.pulsecare.backend.module.patient_queue.mapper;

import com.pulsecare.backend.module.patient_queue.dto.PatientQueueReqDTO;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueResDTO;
import com.pulsecare.backend.module.patient_queue.enums.QueuePriority;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientQueueMapper {

    @Mapping(source = "triage.id", target = "triageId")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.fullName", target = "patientName")
    @Mapping(target = "triageLevel", source = ".", qualifiedByName = "mapTriageLevel")
    PatientQueueResDTO toDTO(PatientQueue entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "triage", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PatientQueue toEntity(PatientQueueReqDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "triage", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(
            PatientQueueReqDTO dto,
            @MappingTarget PatientQueue entity
    );

    @Named("mapTriageLevel")
    default Integer mapTriageLevel(PatientQueue queue) {
        if (queue.getTriage() != null) return queue.getTriage().getTriageLevel();
        if (queue.getPriority() != null) {
            if (queue.getPriority() == QueuePriority.CRITICAL) {
                return 0;
            } else {
                if (queue.getPriority() == QueuePriority.NON_CRITICAL) return 1;
                return 2;
            }
        }
        return 2;
    }

}

