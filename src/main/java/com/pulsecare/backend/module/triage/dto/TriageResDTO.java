package com.pulsecare.backend.module.triage.dto;

import java.time.LocalDateTime;

public record TriageResDTO(
        Long id,
        Long patientId,

        // ========== Basic Categorical Features ==========
        Integer sex, // 0=Female, 1=Male
        Integer arrivalMode, // 1=Walking, 2=Public Ambulance, 3=Private Vehicle, 4=Private Ambulance, 5/6/7=Other
        Integer injury, // 2=Yes, 1=No
        Integer mental, // 1=Alert, 2=Verbal Response, 3=Pain Response, 4=Unresponsive
        Integer pain, // 0=No, 1=Yes

        // ========== Basic Numerical Features ==========
        Integer age,
        Integer sbp,
        Integer dbp,
        Integer hr,
        Integer rr,
        Double bt,

        // ========== Derived Numerical Features ==========
        Double shockIndex,
        Double pulsePressure,
        Double ppRatio,
        Double hrBtInteraction,
        Double rrHrRatio,

        // ========== Derived Categorical Features ==========
        Boolean isFever,
        Boolean isTachy,
        Boolean isLowSbp,
        Boolean isLowDbp,
        Boolean isTachypnea,

        // ========== Target Label (0 = Critical, 1 = Non-Critical) ==========
        Integer triageLevel,
        String severity,

        // ========== Metadata ==========
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}