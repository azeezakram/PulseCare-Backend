package com.pulsecare.backend.module.prescription.mapper;

import com.pulsecare.backend.module.prescription.dto.*;
import com.pulsecare.backend.module.prescription.model.Prescription;
import com.pulsecare.backend.module.prescription.model.PrescriptionItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mapping(source = "admission.id", target = "admissionId")
    @Mapping(source = "patientQueue.id", target = "queueId")
    @Mapping(target = "doctorName", expression = "java(buildDoctorName(entity))")
    PrescriptionSummaryResDTO toSummaryDTO(Prescription entity);

    @Mapping(source = "entity.admission.id", target = "admissionId")
    @Mapping(source = "entity.patientQueue.id", target = "queueId")
    @Mapping(target = "doctorName", expression = "java(buildDoctorName(entity))")
    @Mapping(target = "items", source = "items")
    PrescriptionDetailResDTO toDetailDTO(Prescription entity, List<PrescriptionItemResDTO> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patientQueue", ignore = true)
    @Mapping(target = "admission", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    Prescription toEntity(PrescriptionReqDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patientQueue", ignore = true)
    @Mapping(target = "admission", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntity(PrescriptionReqDTO dto, @MappingTarget Prescription entity);

    @Mapping(source = "prescription.id", target = "prescriptionId")
    PrescriptionItemResDTO toDTO(PrescriptionItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    PrescriptionItem toEntity(PrescriptionItemReqDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    void updateEntity(PrescriptionItemReqDTO dto, @MappingTarget PrescriptionItem entity);

    List<PrescriptionItemResDTO> toItemDTOList(List<PrescriptionItem> items);

    List<PrescriptionItem> toItemEntityList(List<PrescriptionItemReqDTO> items);

    default String buildDoctorName(Prescription entity) {
        if (entity == null || entity.getDoctor() == null) return "—";
        String fn = entity.getDoctor().getFirstName();
        String ln = entity.getDoctor().getLastName();
        String name = ((fn == null ? "" : fn.trim()) + " " + (ln == null ? "" : ln.trim())).trim();
        return name.isEmpty() ? "—" : name;
    }
}
