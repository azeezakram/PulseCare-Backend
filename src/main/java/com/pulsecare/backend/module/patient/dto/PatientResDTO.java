package com.pulsecare.backend.module.patient.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResDTO(
        Long id,
        String fullName,
        LocalDate dob,
        String bloodGroup,
        String nic,
        String gender,
        String phone,
        LocalDateTime createdAt
) {}
