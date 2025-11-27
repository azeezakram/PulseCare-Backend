package com.pulsecare.backend.module.resource.ward.maper;

import com.pulsecare.backend.module.resource.ward.dto.WardReqDTO;
import com.pulsecare.backend.module.resource.ward.dto.WardResDTO;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WardMapper {
    @Mapping(target = "department", ignore = true)
    Ward toEntity(WardReqDTO dto);

    @Mapping(source = "department.id", target = "departmentId")
    WardResDTO toDTO(Ward obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(WardReqDTO dto, @MappingTarget Ward entity);
}
