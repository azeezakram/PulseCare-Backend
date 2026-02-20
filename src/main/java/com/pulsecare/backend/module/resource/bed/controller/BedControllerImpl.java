package com.pulsecare.backend.module.resource.bed.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.bed.dto.BedReqDTO;
import com.pulsecare.backend.module.resource.bed.dto.BedResDTO;
import com.pulsecare.backend.module.resource.bed.service.BedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/bed")
@Validated
public class BedControllerImpl implements BedController {

    private final BedService service;

    public BedControllerImpl(BedService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<BedResDTO>> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Bed with id %d fetched successfully".formatted(id),
                        service.findById(id)
                )
        );
    }

    @Override
    @GetMapping("/by-bedno-wardid")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<BedResDTO>> findByBedNoAndWardId(@RequestParam("wardId") Integer wardId, @RequestParam("bedNo") String bedNo) {
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Bed with bed number %s fetched successfully".formatted(bedNo),
                        service.findByBedNoAndWardId(bedNo, wardId)
                )
        );
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<List<BedResDTO>>> findAll() {
        List<BedResDTO> data = service.findAll();
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Bed data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ResponseBody<BedResDTO>> create(@RequestBody BedReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Bed created successfully",
                        service.save(data)
                ));
    }

    @Override
    @PostMapping("/batch/{wardId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ResponseBody<List<BedResDTO>>> batchCreate(@PathVariable("wardId") Integer wardId, @RequestBody List<BedReqDTO> data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Beds created successfully",
                        service.batchSave(wardId, data)
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'DOCTOR', 'SUPER_DOCTOR', 'NURSE', 'SUPER_NURSE')")
    public ResponseEntity<ResponseBody<BedResDTO>> update(@PathVariable("id") Long id, @RequestBody BedReqDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Bed updated successfully",
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
                        "Bed deleted successfully",
                        null
                ));
    }
}
