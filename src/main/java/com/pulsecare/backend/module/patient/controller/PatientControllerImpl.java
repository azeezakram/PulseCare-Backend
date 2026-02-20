package com.pulsecare.backend.module.patient.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.patient.dto.PatientReqDTO;
import com.pulsecare.backend.module.patient.dto.PatientResDTO;
import com.pulsecare.backend.module.patient.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/patient")
@Validated
public class PatientControllerImpl implements PatientController {

    private final PatientService service;

    public PatientControllerImpl(PatientService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientResDTO>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient with id %d fetched successfully".formatted(id),
                        service.findById(id)
                )
        );
    }

    @Override
    @GetMapping("/nic/{nic}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientResDTO>> findByNic(@PathVariable("nic") String nic) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient with NIC: %s fetched successfully".formatted(nic),
                        service.findByNic(nic)
                )
        );
    }

    @Override
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<List<PatientResDTO>>> findAllAndActive() {
        List<PatientResDTO> data = service.findAllAndActive();
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Patient data fetched successfully",
                        data
                ));
    }

    @Override
    @GetMapping("/active/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientResDTO>> findByIdAndActive(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient with id %d fetched successfully".formatted(id),
                        service.findByIdAndActive(id)
                )
        );
    }

    @Override
    @GetMapping("/nic/active/{nic}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientResDTO>> findByNicAndActive(@PathVariable("nic") String nic) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient with NIC: %s fetched successfully".formatted(nic),
                        service.findByNicAndActive(nic)
                )
        );
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<List<PatientResDTO>>> findAll() {
        List<PatientResDTO> data = service.findAll();
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Patient data fetched successfully",
                        data
                ));
    }


    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientResDTO>> create(@RequestBody PatientReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient created successfully",
                        service.save(data)
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientResDTO>> update(@PathVariable("id") Long id, @RequestBody PatientReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient updated successfully",
                        service.update(id, data)
                ));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ResponseBody<String>> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient deleted successfully",
                        null
                ));
    }
}
