package com.pulsecare.backend.module.user.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.user.dto.UserImageProjection;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.facade.UserFacade;
import com.pulsecare.backend.module.user.mapper.UserMapper;
import com.pulsecare.backend.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserControllerImpl implements UserController {

    private final UserService service;
    private final UserFacade facade;
    private final UserMapper mapper;

    public UserControllerImpl(UserService service, UserFacade facade, @Qualifier("userMapperImpl") UserMapper mapper) {
        this.service = service;
        this.facade = facade;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> findById(@PathVariable("id") String id) {
        UserResponseDTO data = mapper.toDTO(service.findById(id));

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "User fetched successfully",
                        data
                ));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<ResponseBody<List<UserResponseDTO>>> findAll() {
        List<UserResponseDTO> data = service.findAll().stream()
                .map(mapper::toDTO)
                .toList();
        
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Users fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> create(@RequestBody UserRequestDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "User created successfully",
                        facade.createNewUser(data)
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> update(@PathVariable("id") String id,
                                                                @RequestBody UserRequestDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "User updated successfully",
                        facade.updateUser(data, id)
                ));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<String>> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "User deleted successfully",
                        "empty"
                ));
    }

    @Override
    @GetMapping("/{id}/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<byte[]> fetchProfileImage(@PathVariable UUID id) {

        UserImageProjection image = service.getUserProfileImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getImageData());
    }
}
