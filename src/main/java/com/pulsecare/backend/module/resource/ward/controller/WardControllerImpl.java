package com.pulsecare.backend.module.resource.ward.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.facade.DepartmentFacade;
import com.pulsecare.backend.module.resource.department.mapper.DepartmentMapper;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import com.pulsecare.backend.module.resource.ward.dto.WardReqDTO;
import com.pulsecare.backend.module.resource.ward.dto.WardResDTO;
import com.pulsecare.backend.module.resource.ward.facade.WardFacade;
import com.pulsecare.backend.module.resource.ward.maper.WardMapper;
import com.pulsecare.backend.module.resource.ward.service.WardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/department")
@Validated
public class WardControllerImpl implements WardController {

    private final WardService service;
    private final WardFacade facade;
    private final WardMapper mapper;

    public WardControllerImpl(WardService service, WardFacade facade, @Qualifier("wardMapperImpl") WardMapper mapper) {
        this.service = service;
        this.facade = facade;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<WardResDTO>> findById(@PathVariable("id") Integer id) {
        WardResDTO data = mapper.toDTO(service.findById(id));
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Ward data fetched successfully",
                        data
                ));
    }

    @Override
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<List<WardResDTO>>> findAll() {
        List<WardResDTO> data = service.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Ward data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<WardResDTO>> create(@Valid @RequestBody WardReqDTO data) {
        WardResDTO created = facade.createWard(data);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Ward created successfully",
                        created
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<WardResDTO>> update(
            @PathVariable("id") Integer id, @Valid @RequestBody WardReqDTO data) {

        WardResDTO created = facade.updateWard(data, id);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Ward updated successfully",
                        created
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
                        "Ward deleted successfully",
                        null
                ));
    }

}
