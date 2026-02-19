package com.pulsecare.backend.module.patient_admission.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionReqDTO;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionResDTO;
import com.pulsecare.backend.module.patient_admission.service.PatientAdmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/patient-admission")
@Validated
public class PatientAdmissionControllerImpl implements PatientAdmissionController {

    private final PatientAdmissionService service;

    public PatientAdmissionControllerImpl(PatientAdmissionService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientAdmissionResDTO>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient admission with id %d fetched successfully".formatted(id),
                        service.findById(id)
                )
        );
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<List<PatientAdmissionResDTO>>> findAll() {
        List<PatientAdmissionResDTO> data = service.findAll();
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Patient admission data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientAdmissionResDTO>> create(@RequestBody PatientAdmissionReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient admission created successfully",
                        service.save(data)
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PatientAdmissionResDTO>> update(@PathVariable("id") Long id, @RequestBody PatientAdmissionReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Patient admission updated successfully",
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
                        "Patient admission deleted successfully",
                        null
                ));
    }

    @Override
    @GetMapping("/hasActive/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DOCTOR','SUPER_DOCTOR','NURSE','SUPER_NURSE')")
    public ResponseEntity<ResponseBody<Boolean>> hasActiveAdmission(@PathVariable("id") Long id) {

        boolean hasActive = service.hasActiveAdmission(id);

        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        hasActive ? "Patient has an active admission" : "No active admission",
                        hasActive
                )
        );
    }

}
