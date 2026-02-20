package com.pulsecare.backend.module.patient_queue.ws;

import com.pulsecare.backend.module.patient_queue.dto.PatientQueueResDTO;

import java.time.LocalDateTime;

public record PatientQueueEvent(
        PatientQueueEventType type,                 // QUEUE_CREATED / QUEUE_UPDATED / QUEUE_DELETED
        Long queueId,                // always available
        PatientQueueResDTO payload,  // null for delete (optional)
        LocalDateTime sentAt
) {
    public static PatientQueueEvent created(PatientQueueResDTO dto) {
        return new PatientQueueEvent(PatientQueueEventType.QUEUE_CREATED, dto.id(), dto, LocalDateTime.now());
    }

    public static PatientQueueEvent updated(PatientQueueResDTO dto) {
        return new PatientQueueEvent(PatientQueueEventType.QUEUE_UPDATED, dto.id(), dto, LocalDateTime.now());
    }

    public static PatientQueueEvent deleted(Long id) {
        return new PatientQueueEvent(PatientQueueEventType.QUEUE_DELETED, id, null, LocalDateTime.now());
    }
}


