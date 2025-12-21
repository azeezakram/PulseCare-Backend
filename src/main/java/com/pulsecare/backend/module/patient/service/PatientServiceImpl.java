package com.pulsecare.backend.module.patient.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient.dto.PatientReqDTO;
import com.pulsecare.backend.module.patient.dto.PatientResDTO;
import com.pulsecare.backend.module.patient.mapper.PatientMapper;
import com.pulsecare.backend.module.patient.model.Patient;
import com.pulsecare.backend.module.patient.repository.PatientRepository;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueReqDTO;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueResDTO;
import com.pulsecare.backend.module.patient_queue.enums.QueuePriority;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;
import com.pulsecare.backend.module.patient_queue.mapper.PatientQueueMapper;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.patient_queue.repository.PatientQueueRepository;
import com.pulsecare.backend.module.patient_queue.utils.PatientQueueUtils;
import com.pulsecare.backend.module.triage.model.Triage;
import com.pulsecare.backend.module.triage.service.TriageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final TriageService triageService;
    private final PatientMapper mapper;

    public PatientServiceImpl(PatientRepository repository, TriageService triageService, @Qualifier("patientMapper") PatientMapper mapper) {
        this.repository = repository;
        this.triageService = triageService;
        this.mapper = mapper;
    }

    @Override
    public PatientResDTO findById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient with id " + id + " not found")));
    }

    @Override
    public List<PatientResDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Patient findEntityById(Long id) {
        return null;
    }

    @Override
    public PatientResDTO save(PatientReqDTO data) {
        return null;
    }

    @Override
    public PatientResDTO update(Long aLong, PatientReqDTO data) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }


}
