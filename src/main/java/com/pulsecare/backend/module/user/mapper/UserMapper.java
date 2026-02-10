package com.pulsecare.backend.module.user.mapper;

import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.model.Users;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    Users toEntity(UserRequestDTO dto);

    @Mapping(target = "imageUrl",
            expression = "java( (entity.getImageData() != null && entity.getImageData().length > 0) ? \"/user/\" + entity.getId() + \"/image\" : null )")
    UserResponseDTO toDTO(Users entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UserRequestDTO dto, @MappingTarget Users entity);
}
