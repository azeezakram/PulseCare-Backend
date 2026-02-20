package com.pulsecare.backend.module.patient_admission.mapper;

import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionReqDTO;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionResDTO;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientAdmissionMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.fullName", target = "patientName")
    @Mapping(source = "patientQueue.id", target = "queueId")
    @Mapping(source = "bed.id", target = "bedId")
    @Mapping(source = "status", target = "status")
    PatientAdmissionResDTO toDTO(PatientAdmission entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "patientQueue", ignore = true)
    @Mapping(target = "bed", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "admittedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PatientAdmission toEntity(PatientAdmissionReqDTO dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "patientQueue", ignore = true)
    @Mapping(target = "bed", ignore = true)
    @Mapping(target = "admittedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(
            PatientAdmissionReqDTO dto,
            @MappingTarget PatientAdmission entity
    );
}
