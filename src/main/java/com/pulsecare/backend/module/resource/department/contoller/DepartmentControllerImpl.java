package com.pulsecare.backend.module.resource.department.contoller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/department")
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
    public ResponseEntity<ResponseBody<DeptResponseDTO>> findAll() {
        return null;
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> create(@Valid @RequestBody DeptRequestDTO data, BindingResult result) {
        try {
            DeptResponseDTO created = service.create(data);

            if (result.hasErrors()) {
                String errors = result.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));

                return ResponseEntity.badRequest().body(
                        new ResponseBody<>(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                errors,
                                null
                        ));
            }

            return ResponseEntity
                    .ok()
                    .body(new ResponseBody<>(
                            HttpStatus.OK.value(),
                            "Department successfully created",
                            created
                    ));

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ResponseBody<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal server error: " + e.getMessage(),
                            null
                    ));
        }
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
