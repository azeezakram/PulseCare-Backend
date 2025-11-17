package com.pulsecare.backend.module.user.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.service.UserService;
import com.pulsecare.backend.utils.Validation;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
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
        return null;
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> create(@RequestBody UserRequestDTO data, BindingResult result) {
        try {
            UserResponseDTO created = userService.create(data);

            if (result.hasErrors()) {
                String errors = result.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));

                return ResponseEntity.badRequest().body(
                        new ResponseBody<>(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                errors,
                                null
                        ));
            }

            return ResponseEntity
                    .ok()
                    .body(new ResponseBody<>(
                            HttpStatus.OK.value(),
                            "Department successfully created",
                            created
                    ));

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ResponseBody<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal server error: " + e.getMessage(),
                            null
                    ));
        }
    }

    @Override
    @PutMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<UserResponseDTO>> update(@RequestBody UserRequestDTO data, BindingResult result) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<Byte>> delete(@PathVariable("id") String id) {
        return null;
    }

}
