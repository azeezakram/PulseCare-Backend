package com.pulsecare.backend.module.patient_admission.dto;

import com.pulsecare.backend.module.patient_admission.enums.PatientAdmissionStatus;

public record PatientAdmissionReqDTO(

        Long patientId,          // required when admitting
        Long queueId,            // optional (null if direct admission)
        Integer wardId,           // required when admitting
        Integer bedNo,            // required when admitting

        // used mainly for discharge
        PatientAdmissionStatus status,   // ACTIVE / DISCHARGED
        String dischargeNotes

) {}
