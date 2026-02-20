package com.pulsecare.backend.module.patient_queue.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.module.patient.model.Patient;
import com.pulsecare.backend.module.patient.service.PatientService;
import com.pulsecare.backend.module.patient_admission.service.PatientAdmissionService;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueReqDTO;
import com.pulsecare.backend.module.patient_queue.dto.PatientQueueResDTO;
import com.pulsecare.backend.module.patient_queue.enums.QueuePriority;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;
import com.pulsecare.backend.module.patient_queue.mapper.PatientQueueMapper;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.patient_queue.repository.PatientQueueRepository;
import com.pulsecare.backend.module.patient_queue.utils.PatientQueueUtils;
import com.pulsecare.backend.module.patient_queue.ws.PatientQueueEvent;
import com.pulsecare.backend.module.patient_queue.ws.PatientQueueEventPublisher;
import com.pulsecare.backend.module.triage.model.Triage;
import com.pulsecare.backend.module.triage.service.TriageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientQueueServiceImpl implements PatientQueueService {

    private final PatientQueueRepository repository;
    private final PatientQueueMapper mapper;
    private final TriageService triageService;
    private final PatientService patientService;
    private final PatientAdmissionService patientAdmissionService;
    private final PatientQueueEventPublisher publisher;

    public PatientQueueServiceImpl(PatientQueueRepository repository, @Qualifier("patientQueueMapperImpl") PatientQueueMapper mapper,
                                   TriageService triageService, PatientService patientService, @Lazy PatientAdmissionService patientAdmissionService, PatientQueueEventPublisher publisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.triageService = triageService;
        this.patientService = patientService;
        this.patientAdmissionService = patientAdmissionService;
        this.publisher = publisher;
    }

    
    @Override
    public PatientQueueResDTO findById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Queue with id " + id + " not found")));
    }

    @Override
    public PatientQueue findEntityById(Long id) {
        return repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Queue with id " + id + " not found"));
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
        if (data.patientId() == null) {
            throw new ValidationException("Patient is required to create a queue record");
        }
        if (patientAdmissionService.hasActiveAdmission(data.patientId())) {
            throw new ResourceAlreadyExistsException("Patient already has an active admission");
        }
        PatientQueue entity = mapper.toEntity(data);

        if (data.triageId() != null) {
            Triage exist = triageService.findEntityById(data.triageId());
            entity.setTriage(exist);
            entity.setPriority(exist.getTriageLevel() == 0 ? QueuePriority.CRITICAL : QueuePriority.NON_CRITICAL);
        } else {
            entity.setPriority(QueuePriority.valueOf(data.priority().name()));
        }

        Patient patient = patientService.findEntityById(data.patientId());
        entity.setPatient(patient);

        entity.setStatus(QueueStatus.WAITING);

        PatientQueue saved = repository.save(entity);
        PatientQueueResDTO dto = mapper.toDTO(saved);

        publisher.publish(PatientQueueEvent.created(dto));

        return dto;
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
            existing.setPriority(triage.getTriageLevel() == 0 ? QueuePriority.CRITICAL : QueuePriority.NON_CRITICAL);
        } else if (data.priority() != null) {
            existing.setPriority(data.priority());
        }

        if (data.status() != null) {
            PatientQueueUtils.validateStatusTransition(existing.getStatus(), data.status());
            existing.setStatus(data.status());
        }

        if (data.patientId() != null) {
            Patient patient = patientService.findEntityById(data.patientId());
            existing.setPatient(patient);
        }

        PatientQueue saved = repository.save(existing);
        PatientQueueResDTO dto = mapper.toDTO(saved);

        publisher.publish(PatientQueueEvent.updated(dto));

        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PatientQueue entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        repository.delete(entity);

        publisher.publish(PatientQueueEvent.deleted(id));
    }

}
