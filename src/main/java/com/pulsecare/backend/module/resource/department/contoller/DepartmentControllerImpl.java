package com.pulsecare.backend.module.resource.department.contoller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.facade.DepartmentFacade;
import com.pulsecare.backend.module.resource.department.mapper.DepartmentMapper;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
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
public class DepartmentControllerImpl implements DepartmentController {

    private final DepartmentService service;
    private final DepartmentFacade facade;
    private final DepartmentMapper mapper;

    public DepartmentControllerImpl(DepartmentService service, DepartmentFacade facade, @Qualifier("departmentMapperImpl") DepartmentMapper mapper) {
        this.service = service;
        this.facade = facade;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> findById(@PathVariable("id") Integer id) {
        DeptResponseDTO data = mapper.toDTO(service.findById(id));
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Department data successfully fetched",
                        data
                ));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ResponseBody<List<DeptResponseDTO>>> findAll() {
        List<DeptResponseDTO> data = service.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Department data successfully fetched",
                        data
                ));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> create(@Valid @RequestBody DeptRequestDTO data) {
        DeptResponseDTO created = facade.createDepartment(data);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Department successfully created",
                        created
                ));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> update(
            @PathVariable("id") Integer id, @Valid @RequestBody DeptRequestDTO data) {

        DeptResponseDTO created = facade.updateDepartment(data, id);
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Department updated successfully",
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
                        "Department deleted successfully",
                        null
                ));
    }
}
