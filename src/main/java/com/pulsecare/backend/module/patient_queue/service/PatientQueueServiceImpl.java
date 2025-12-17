package com.pulsecare.backend.module.patient_queue.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueReqDTO;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueResDTO;
import com.pulsecare.backend.module.patient_queue.mapper.PatientQueueMapper;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.patient_queue.repository.PatientQueueRepository;
import com.pulsecare.backend.module.triage.model.Triage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientQueueServiceImpl implements PatientQueueService {

    private final PatientQueueRepository repository;
    private final PatientQueueMapper mapper;

    public PatientQueueServiceImpl(PatientQueueRepository repository, PatientQueueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PatientQueueResDTO findById(Long id) {
        PatientQueue data = repository.findById(id).orElse(null);
        if (data == null) {
            throw new ResourceNotFoundException("Queue with id " + id + " not found");
        }
        return mapper.toDTO(data);
    }

    @Override
    public List<PatientQueueResDTO> findAll() {
        return List.of();
    }

    @Override
    public PatientQueueResDTO save(PatientQueueReqDTO data) {
        return null;
    }

    @Override
    public PatientQueueResDTO update(Long aLong, PatientQueueReqDTO data) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
