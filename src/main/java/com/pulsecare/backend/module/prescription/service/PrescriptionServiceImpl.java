package com.pulsecare.backend.module.prescription.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.prescription.dto.PrescriptionDetailResDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionReqDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionSummaryResDTO;
import com.pulsecare.backend.module.prescription.mapper.PrescriptionMapper;
import com.pulsecare.backend.module.prescription.model.Prescription;
import com.pulsecare.backend.module.prescription.repository.PrescriptionItemRepository;
import com.pulsecare.backend.module.prescription.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionMapper mapper;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, PrescriptionItemRepository prescriptionItemRepository,
                                   @Qualifier("prescriptionMapperImpl") PrescriptionMapper mapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionItemRepository = prescriptionItemRepository;
        this.mapper = mapper;
    }

    @Override
    public PrescriptionSummaryResDTO findById(Long id) {
        return mapper.toSummaryDTO(
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient admission with id " + id + " not found"))
        );
    }

    @Override
    public PrescriptionDetailResDTO findWithDetailById(Long id) {
        return null;
    }

    @Override
    public Prescription findEntityById(Long id) {
        return null;
    }

    @Override
    public List<PrescriptionSummaryResDTO> findAll() {
        return List.of();
    }

    @Override
    public PrescriptionDetailResDTO save(PrescriptionReqDTO data) {
        return null;
    }

    @Override
    public PrescriptionDetailResDTO update(Long aLong, PrescriptionReqDTO data) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

}
