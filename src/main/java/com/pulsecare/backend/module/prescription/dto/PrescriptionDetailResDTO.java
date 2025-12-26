package com.pulsecare.backend.module.prescription.dto;

import com.pulsecare.backend.module.prescription.enums.PrescriptionType;

import java.time.LocalDateTime;
import java.util.List;

public record PrescriptionDetailResDTO(
        Long id,
        String doctorName,
        Long admissionId,
        Long queueId,
        PrescriptionType type,
        String notes,
        String status,
        List<PrescriptionItemResDTO> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

