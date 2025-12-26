package com.pulsecare.backend.module.prescription.dto;

import com.pulsecare.backend.module.prescription.enums.PrescriptionType;

import java.util.List;

public record PrescriptionReqDTO(
        String doctorId,
        Long queueId,
        Long admissionId,
        PrescriptionType type,
        String notes,
        List<PrescriptionItemReqDTO> items
) {}

