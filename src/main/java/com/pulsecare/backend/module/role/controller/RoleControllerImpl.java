package com.pulsecare.backend.module.role.controller;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.role.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/role")
@Validated
public class RoleControllerImpl implements RoleController {

    private final RoleService userService;

    public RoleControllerImpl(RoleService userService) {
        this.userService = userService;
    }


    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<RoleResDto>> findById(@PathVariable("id") Integer id) {
        return null;
    }

    @Override
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<List<RoleResDto>>> findAll() {
        return null;
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<RoleResDto>> create(@Valid @RequestBody RoleReqDto data, BindingResult result) {

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }

        RoleResDto created = userService.create(data);

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Department successfully created",
                        created
                ));

    }

    @Override
    @PutMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<RoleResDto>> update(@Valid @RequestBody RoleReqDto data, BindingResult result) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<Byte>> delete(@PathVariable("id") Integer id) {
        return null;
    }

}
