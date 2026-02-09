package com.pulsecare.backend.module.triage.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.triage.dto.TriageReqDTO;
import com.pulsecare.backend.module.triage.dto.TriageResDTO;
import com.pulsecare.backend.module.triage.service.TriageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/triage")
@Validated
public class TriageControllerImpl implements TriageController {

    private final TriageService service;

    public TriageControllerImpl(TriageService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<TriageResDTO>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Triage fetched successfully",
                        service.findById(id)
                )
        );
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<List<TriageResDTO>>> findAll() {
        List<TriageResDTO> data = service.findAll();
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Triage data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<TriageResDTO>> create(@RequestBody TriageReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Triage created successfully",
                        service.save(data)
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ResponseBody<TriageResDTO>> update(@PathVariable("id") Long id, @RequestBody TriageReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Triage updated successfully",
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
                        "Triage deleted successfully",
                        null
                ));
    }

    @Override
    @PostMapping("/predict")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<TriageResDTO>> predict(@RequestBody TriageReqDTO dto) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Triage predicted successfully",
                        service.predict(dto)
                ));
    }
}
