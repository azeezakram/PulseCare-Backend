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
    public ResponseEntity<ResponseBody<WardResDTO>> findById(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBody<List<WardResDTO>>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBody<WardResDTO>> create(WardReqDTO data) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBody<String>> delete(Integer integer) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBody<WardResDTO>> update(Integer integer, WardReqDTO data) {
        return null;
    }
}
