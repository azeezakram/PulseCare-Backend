package com.pulsecare.backend.module.patient_queue.dto;

import com.pulsecare.backend.module.patient_queue.enums.QueuePriority;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;

public record PatientQueueReqDTO(
        String patientName,
        Integer age,

        Long triageId,

        // optional manual priority; defaults handled in service
        QueuePriority priority,  // CRITICAL | NON_CRITICAL | NORMAL

        QueueStatus status
) {}

