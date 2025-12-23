package com.pulsecare.backend.module.patient_admission.dto;

import java.time.LocalDateTime;

public record PatientAdmissionResDTO(

        Long id,

        Long patientId,
        String patientName,

        Long queueId,

        Integer wardId,
        String wardName,

        Integer bedNo,

        String status,
        LocalDateTime admittedAt,
        LocalDateTime dischargedAt,

        String dischargeNotes

) {}
