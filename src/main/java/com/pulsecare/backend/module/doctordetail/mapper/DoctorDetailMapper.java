package com.pulsecare.backend.module.doctordetail.mapper;

import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailReqDto;
import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailResDto;
import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DoctorDetailMapper {

    @Mapping(target = "specializations", ignore = true)
    @Mapping(target = "user", ignore = true)
    DoctorDetail toEntity(DoctorDetailReqDto dto);

    @Mapping(source = "user.id", target = "userId")
    DoctorDetailResDto toDTO(DoctorDetail entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specializations", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DoctorDetailReqDto dto, @MappingTarget DoctorDetail entity);
}
