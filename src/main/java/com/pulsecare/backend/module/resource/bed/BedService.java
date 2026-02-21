package com.pulsecare.backend.module.resource.bed;

import com.pulsecare.backend.common.base.service.DeletableService;
import com.pulsecare.backend.common.base.service.FindableService;
import com.pulsecare.backend.common.base.service.SavableService;
import com.pulsecare.backend.common.base.service.UpdatableService;
import com.pulsecare.backend.module.resource.bed.dto.BedReqDTO;
import com.pulsecare.backend.module.resource.bed.dto.BedResDTO;
import com.pulsecare.backend.module.resource.bed.model.Bed;

import java.util.List;

public interface BedService extends
        FindableService<Long, BedResDTO>,
        SavableService<BedReqDTO, BedResDTO>,
        UpdatableService<BedReqDTO, BedResDTO, Long>,
        DeletableService<Long> {
    Bed findEntityById(Long id);
    BedResDTO findByBedNoAndWardId(String bedNo, Integer wardId);
    List<BedResDTO> batchSave(Integer wardId, List<BedReqDTO> beds);
}
