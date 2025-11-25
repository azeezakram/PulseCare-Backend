package com.pulsecare.backend.module.doctordetail.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailReqDto;
import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailResDto;
import com.pulsecare.backend.module.doctordetail.facade.DoctorDetailFacade;
import com.pulsecare.backend.module.doctordetail.mapper.DoctorDetailMapper;
import com.pulsecare.backend.module.doctordetail.service.DoctorDetailService;
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
@RequestMapping("/api/v1/doctor-detail")
@Validated
public class DoctorDetailControllerImpl implements DoctorDetailController {

    private final DoctorDetailService service;
    private final DoctorDetailFacade facade;
    private final DoctorDetailMapper mapper;

    public DoctorDetailControllerImpl(DoctorDetailService service, DoctorDetailFacade facade,
                                      @Qualifier("doctorDetailMapperImpl") DoctorDetailMapper mapper) {
        this.service = service;
        this.facade = facade;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> findById(@PathVariable("id") Long id) {
        DoctorDetailResDto data = mapper.toDTO(service.findById(id));
        return ResponseEntity.ok().body(
                new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Doctor details fetched successfully",
                        data
                )
        );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> findById(@PathVariable("userId") String userId) {
        DoctorDetailResDto data = mapper.toDTO(service.findByUserId(userId));
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
        List<DoctorDetailResDto> data = service.findAll().stream()
                .map(mapper::toDTO)
                .toList();

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Doctor details data fetched successfully",
                        data
                ));
    }

    @Override
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> create(@Valid @RequestBody DoctorDetailReqDto data) {
        DoctorDetailResDto created = facade.create(data);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Doctor details created successfully",
                        created
                ));
    }


    // Update by Doctor detail ID
    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> update(@PathVariable("id") Long id,
                                                                    @RequestBody DoctorDetailReqDto data) {
        DoctorDetailResDto updated = mapper.toDTO(
                service.update(id, mapper.toEntity(data))
        );
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Doctor details updated successfully",
                        updated
                ));
    }

    // Update by User ID
    @Override
    @PutMapping("/u/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ResponseBody<DoctorDetailResDto>> update(@PathVariable("userId") String userId,
                                                                    @RequestBody DoctorDetailReqDto data) {
        DoctorDetailResDto updated = facade.update(data, userId);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Doctor details updated successfully",
                        updated
                ));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<String>> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Doctor details deleted successfully",
                        "empty"
                ));
    }
}
