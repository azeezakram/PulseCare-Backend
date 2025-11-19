package com.pulsecare.backend.module.resource.department.contoller;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        List<DeptResponseDTO> created = service.findAll();

        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        created.isEmpty() ? "No data to fetched" : "Department data successfully fetched",
                        created
                ));
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> create(@RequestBody DeptRequestDTO data, BindingResult result) {

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }

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
    @PutMapping("/")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> update(@RequestBody DeptRequestDTO data, BindingResult result) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<Byte>> delete(@PathVariable("id") Integer id) {
        return null;
    }
}
