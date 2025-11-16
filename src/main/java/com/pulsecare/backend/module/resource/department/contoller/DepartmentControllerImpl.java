package com.pulsecare.backend.module.resource.department.contoller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentControllerImpl implements DepartmentController {

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
    public ResponseEntity<ResponseBody<DeptResponseDTO>> create(@RequestBody DeptRequestDTO data) {
        return null;
    }

    @Override
    @PutMapping("/")
    public ResponseEntity<ResponseBody<DeptResponseDTO>> update(@RequestBody DeptRequestDTO data) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<Byte>> delete(@PathVariable("id") Integer id) {
        return null;
    }
}
