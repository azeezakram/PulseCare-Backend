package com.pulsecare.backend.module.triage.dto;

import jakarta.validation.constraints.*;

public record TriageReqDTO(
//        @NotEmpty(message = "Patient id is required")
        Long patientId,

        @NotNull(message = "Sex is required")
        @Min(value = 0, message = "Sex must be 0 (Female) or 1 (Male)")
        @Max(value = 1, message = "Sex must be 0 (Female) or 1 (Male)")
        Integer sex, // 0=Female, 1=Male

        @NotNull(message = "Arrival mode is required")
        @Min(value = 1, message = "Arrival mode must be between 1 and 7")
        @Max(value = 7, message = "Arrival mode must be between 1 and 7")
        Integer arrivalMode, // 1=Walking, 2=Public Ambulance, 3=Private Vehicle, 4=Private Ambulance, 5/6/7=Other

        @NotNull(message = "Injury is required")
        @Min(value = 1, message = "Injury must be 1 (Yes) or 2 (No)")
        @Max(value = 2, message = "Injury must be 1 (Yes) or 2 (No)")
        Integer injury, // 1=Yes, 2=No

        @NotNull(message = "Mental status is required")
        @Min(value = 1, message = "Mental status must be between 1 and 4")
        @Max(value = 4, message = "Mental status must be between 1 and 4")
        Integer mental, // 1=Alert, 2=Verbal Response, 3=Pain Response, 4=Unresponsive

        @NotNull(message = "Pain is required")
        @Min(value = 0, message = "Pain must be 0 (No) or 1 (Yes)")
        @Max(value = 1, message = "Pain must be 0 (No) or 1 (Yes)")
        Integer pain, // 0=No, 1=Yes

        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age must be positive")
        @Max(value = 150, message = "Age must be realistic")
        Integer age,

        @NotNull(message = "Systolic blood pressure is required")
        @Min(value = 50, message = "SBP must be at least 50")
        @Max(value = 250, message = "SBP must not exceed 250")
        Integer sbp,

        @NotNull(message = "Diastolic blood pressure is required")
        @Min(value = 30, message = "DBP must be at least 30")
        @Max(value = 150, message = "DBP must not exceed 150")
        Integer dbp,

        @NotNull(message = "Heart rate is required")
        @Min(value = 30, message = "HR must be at least 30")
        @Max(value = 250, message = "HR must not exceed 250")
        Integer hr,

        @NotNull(message = "Respiratory rate is required")
        @Min(value = 5, message = "RR must be at least 5")
        @Max(value = 60, message = "RR must not exceed 60")
        Integer rr,

        @NotNull(message = "Body temperature is required")
        @DecimalMin(value = "35.0", message = "BT must be at least 35.0")
        @DecimalMax(value = "42.0", message = "BT must not exceed 42.0")
        Double bt
) {}