package com.pulsecare.backend.module.patient_queue.utils;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PatientQueueUtils {

    public static void validateStatusTransition(QueueStatus current, QueueStatus next) {

        if (current == next) {
            return;
        }

        if (current == QueueStatus.WAITING &&
                (next == QueueStatus.ADMITTED || next == QueueStatus.CANCELLED || next == QueueStatus.OUTPATIENT)) {
            return;
        }

        throw new ValidationException(
                "Invalid queue status transition: " + current + " -> " + next
        );
    }

}
