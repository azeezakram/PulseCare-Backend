package com.pulsecare.backend.module.patient_queue.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
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
public class PatientQueueServiceImpl implements PatientQueueService {

    private final PatientQueueRepository repository;
    private final PatientQueueMapper mapper;
    private final TriageService triageService;

    public PatientQueueServiceImpl(PatientQueueRepository repository, @Qualifier("patientQueueMapperImpl") PatientQueueMapper mapper, TriageService triageService) {
        this.repository = repository;
        this.mapper = mapper;
        this.triageService = triageService;
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
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public PatientQueueResDTO save(PatientQueueReqDTO data) {
        PatientQueue entity = mapper.toEntity(data);

        if (data.triageId() != null) {
            Triage exist = triageService.findEntityById(data.triageId());

            entity.setTriage(exist);

            entity.setPriority(
                    exist.getTriageLevel() == 0
                            ? QueuePriority.CRITICAL
                            : QueuePriority.NON_CRITICAL
            );
        } else {
            entity.setPriority(QueuePriority.NORMAL);
        }

        entity.setStatus(QueueStatus.WAITING);

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    @Transactional
    public PatientQueueResDTO update(Long id, PatientQueueReqDTO data) {
        PatientQueue existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        mapper.updateEntity(data, existing);

        if (data.triageId() != null) {
            Triage triage = triageService.findEntityById(data.triageId());
            existing.setTriage(triage);

            existing.setPriority(
                    triage.getTriageLevel() == 0 ? QueuePriority.CRITICAL : QueuePriority.NON_CRITICAL
            );
        } else if (data.priority() != null) {
            existing.setPriority(data.priority());
        }

        if (data.status() != null) {
            PatientQueueUtils.validateStatusTransition(existing.getStatus(), data.status());
            existing.setStatus(data.status());
        }

        return mapper.toDTO(repository.save(existing));
    }


    @Override
    public void delete(Long id) {
        PatientQueue entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        repository.delete(entity);
    }
}
