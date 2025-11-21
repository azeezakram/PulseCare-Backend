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
@RequestMapping("/role")
@Validated
public class RoleControllerImpl implements RoleController {

    private final RoleService service;

    public RoleControllerImpl(RoleService userService) {
        this.service = userService;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<RoleResDto>> findById(@PathVariable("id") Integer id) {
        RoleResDto data = service.findById(id);
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Role fetched successfully",
                        data
                )
        );
    }

    @Override
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<List<RoleResDto>>> findAll() {
        List<RoleResDto> data = service.findAll();

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Role data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<RoleResDto>> create(@Valid @RequestBody RoleReqDto data, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        RoleResDto created = service.create(data);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Role created successfully",
                        created
                ));

    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<RoleResDto>> update(@Valid @PathVariable("id") Integer id,
                                                           @RequestBody RoleReqDto data, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        RoleResDto updated = service.update(id, data);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Role updated successfully",
                        updated
                ));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<String>> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Role deleted successfully",
                        null
                ));
    }

}
