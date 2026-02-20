package com.pulsecare.backend.module.prescription.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.prescription.dto.PrescriptionDetailResDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionReqDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionSummaryResDTO;
import com.pulsecare.backend.module.prescription.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/prescription")
@Validated
public class PrescriptionControllerImpl implements PrescriptionController {

    private final PrescriptionService service;

    public PrescriptionControllerImpl(PrescriptionService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PrescriptionSummaryResDTO>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Prescription with id %d fetched successfully".formatted(id),
                        service.findById(id)
                )
        );
    }

    @Override
    @GetMapping("/d/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PrescriptionDetailResDTO>> findWithDetailById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Prescription with id %d fetched successfully".formatted(id),
                        service.findWithDetailById(id)
                )
        );
    }

    @Override
    @GetMapping("/by-admission/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<List<PrescriptionDetailResDTO>>> findAllByAdmissionsId(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Prescriptions with admission id %d fetched successfully".formatted(id),
                        service.findAllByAdmissionsId(id)
                )
        );
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<List<PrescriptionSummaryResDTO>>> findAll() {
        List<PrescriptionSummaryResDTO> data = service.findAll();
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Prescription data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PrescriptionDetailResDTO>> create(@RequestBody PrescriptionReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Prescription created successfully",
                        service.save(data)
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<PrescriptionDetailResDTO>> update(@PathVariable("id") Long id, @RequestBody PrescriptionReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Prescription updated successfully",
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
                        "Prescription deleted successfully",
                        null
                ));
    }
}
