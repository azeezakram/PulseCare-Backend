package com.pulsecare.backend.module.patient_queue.dto;

import java.time.LocalDateTime;

public record PatientQueueResDTO(
        Long id,

        String patientName,
        Long patientId,

        Long triageId,
        Integer triageLevel, // 0 / 1 (nullable)

        String status,   // WAITING / ADMITTED
        String priority, // CRITICAL / NON_CRITICAL / NORMAL
        Boolean admitted,

        LocalDateTime createdAt
) {}

