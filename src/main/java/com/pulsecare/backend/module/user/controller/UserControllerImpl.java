package com.pulsecare.backend.module.user.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailResDto;
import com.pulsecare.backend.module.role.service.RoleService;
import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.facade.UserFacade;
import com.pulsecare.backend.module.user.mapper.UserMapper;
import com.pulsecare.backend.module.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserControllerImpl implements UserController {

    private final UserService service;
    private final UserFacade facade;
    private final UserMapper mapper;

    public UserControllerImpl(UserService service, UserFacade facade, UserMapper mapper) {
        this.service = service;
        this.facade = facade;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> findById(@PathVariable("id") String id) {

        return null;
    }

    @Override
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<List<UserResponseDTO>>> findAll() {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "User successfully created",
                        service.findAll().stream()
                                .map(mapper::toDTO)
                                .toList()
                ));
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> create(@RequestBody UserRequestDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "User successfully created",
                        facade.createNewUser(data)
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> update(@Valid @PathVariable("id") String id,
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
        return null;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<ResponseBody<String>> login(@RequestBody LoginRequestDTO data) {
        String token = service.login(data);

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "success",
                        token
                ));
    }
}
