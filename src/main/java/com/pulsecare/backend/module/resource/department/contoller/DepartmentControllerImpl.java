package com.pulsecare.backend.module.resource.department.contoller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/department")
@Validated
public class DepartmentControllerImpl implements DepartmentController {

    private final DepartmentService service;

    public DepartmentControllerImpl(DepartmentService service) {
        this.service = service;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> findById(@PathVariable("id") Integer id) {
        return null;
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<ResponseBody<List<DeptResponseDTO>>> findAll() {
        List<DeptResponseDTO> data = service.findAll();

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        data.isEmpty() ? "No data to fetched" : "Department data successfully fetched",
                        data
                ));
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> create(@RequestBody DeptRequestDTO data) {
        DeptResponseDTO created = service.create(data);
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
    public ResponseEntity<ResponseBody<DeptResponseDTO>> update(
            @Valid @PathVariable("id") Integer id, @RequestBody DeptRequestDTO data) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<String>> delete(@PathVariable("id") Integer id) {
        return null;
    }
}
