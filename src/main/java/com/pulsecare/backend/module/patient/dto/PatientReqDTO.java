package com.pulsecare.backend.module.patient.dto;

import java.time.LocalDate;

public record PatientReqDTO(
        String fullName,
        LocalDate dob,
        String bloodGroup,
        String nic,
        String gender,
        String phone
) {}

