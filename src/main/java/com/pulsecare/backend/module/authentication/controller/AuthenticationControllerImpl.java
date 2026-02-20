package com.pulsecare.backend.module.authentication.controller;

import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.authentication.dto.LoginRequestDTO;
import com.pulsecare.backend.module.authentication.dto.LoginResponseDTO;
import com.pulsecare.backend.module.authentication.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationControllerImpl(AuthenticationService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<ResponseBody<LoginResponseDTO>> login(@RequestBody LoginRequestDTO data) {
        return ResponseEntity
                .ok()
                .body(new ResponseBody<>(
                        HttpStatus.OK.value(),
                        "Logged in successfully",
                        service.login(data)
                ));
    }

    @Override
    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyByPassword(@RequestBody LoginRequestDTO data) {
        return ResponseEntity
                .ok()
                .body(service.verifyByPassword(data));
    }
}
