package com.pulsecare.backend.module.specialization.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.specialization.dto.SpecializationReqDTO;
import com.pulsecare.backend.module.specialization.dto.SpecializationResDTO;
import com.pulsecare.backend.module.specialization.service.SpecializationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/specialization")
@Validated
public class SpecializationControllerImpl implements SpecializationController {

    private final SpecializationService service;

    public SpecializationControllerImpl(SpecializationService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<SpecializationResDTO>> findById(@PathVariable("id") Integer id) {
        SpecializationResDTO data = service.findById(id);
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Specialization fetched successfully",
                        data
                )
        );
    }

    @Override
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<List<SpecializationResDTO>>> findAll() {
        List<SpecializationResDTO> data = service.findAll();

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Specialization data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<SpecializationResDTO>> create(@Valid @RequestBody SpecializationReqDTO data) {
        SpecializationResDTO created = service.create(data);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Specialization created successfully",
                        created
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<SpecializationResDTO>> update(@Valid @PathVariable("id") Integer id,
                                                                     @RequestBody SpecializationReqDTO data) {
        SpecializationResDTO updated = service.update(id, data);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Specialization updated successfully",
                        updated
                ));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<String>> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Specialization deleted successfully",
                        null
                ));
    }
}
