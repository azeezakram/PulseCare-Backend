package com.pulsecare.backend.module.triage.service;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.triage.dto.TriagePredictionReqDTO;
import com.pulsecare.backend.module.triage.model.Triage;

public interface TriageService extends
        FindableService<Long, Triage>,
        SavableService<Triage, Triage>,
        UpdatableService<Triage, Triage, Long>,
        DeletableService<Long> {
    Triage predict(Triage dto);
}
