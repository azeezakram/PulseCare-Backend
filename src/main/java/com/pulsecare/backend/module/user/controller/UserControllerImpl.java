package com.pulsecare.backend.module.user.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin")
public class UserControllerImpl implements UserController {

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<UserResponseDTO>> findById(@PathVariable("id") String id) {
        return null;
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<ResponseBody<UserResponseDTO>> findAll() {
        return null;
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<ResponseBody<UserResponseDTO>> create(@RequestBody UserRequestDTO data) {
        return null;
    }

    @Override
    @PutMapping("/")
    public ResponseEntity<ResponseBody<UserResponseDTO>> update(@RequestBody UserRequestDTO data) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody<Byte>> delete(@PathVariable("id") String id) {
        return null;
    }

}
