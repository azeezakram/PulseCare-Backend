package com.pulsecare.backend.module.prescription.dto;

import com.pulsecare.backend.module.prescription.enums.PrescriptionType;

import java.time.LocalDateTime;

public record PrescriptionSummaryResDTO(
        Long id,
        String doctorName,
        Long admissionId,
        Long queueId,
        PrescriptionType type,
        String notes,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
