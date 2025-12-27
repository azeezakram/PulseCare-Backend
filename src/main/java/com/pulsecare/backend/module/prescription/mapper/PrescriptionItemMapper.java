package com.pulsecare.backend.module.prescription.mapper;

import com.pulsecare.backend.module.prescription.dto.PrescriptionItemReqDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionItemResDTO;
import com.pulsecare.backend.module.prescription.model.PrescriptionItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrescriptionItemMapper {

    @Mapping(source = "prescription.id", target = "prescriptionId")
    PrescriptionItemResDTO toDTO(PrescriptionItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    PrescriptionItem toEntity(PrescriptionItemReqDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PrescriptionItemReqDTO dto,
                      @MappingTarget PrescriptionItem entity);

}

