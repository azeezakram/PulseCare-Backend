package com.pulsecare.backend.module.doctordetail.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailReqDto;
import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailResDto;
import com.pulsecare.backend.module.doctordetail.service.DoctorDetailService;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/doctor-detail")
@Validated
public class DoctorDetailControllerImpl implements DoctorDetailController {

    private final DoctorDetailService service;

    public DoctorDetailControllerImpl(DoctorDetailService service) {
        this.service = service;
    }


    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> findById(@PathVariable("id") Long id) {
        DoctorDetailResDto data = service.findById(id);
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Doctor details fetched successfully",
                        data
                )
        );
    }

    @Override
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<List<DoctorDetailResDto>>> findAll() {
        return null;
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> create(@Valid @RequestBody DoctorDetailReqDto data) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> update(@Valid @PathVariable("id") Long id,
                                                                     @RequestBody DoctorDetailReqDto data) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<String>> delete(@PathVariable("id") Long id) {
        return null;
    }
}
