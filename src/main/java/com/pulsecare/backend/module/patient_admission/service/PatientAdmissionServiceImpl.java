package com.pulsecare.backend.module.patient_admission.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient.model.Patient;
import com.pulsecare.backend.module.patient.service.PatientService;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionReqDTO;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionResDTO;
import com.pulsecare.backend.module.patient_admission.enums.PatientAdmissionStatus;
import com.pulsecare.backend.module.patient_admission.mapper.PatientAdmissionMapper;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import com.pulsecare.backend.module.patient_admission.repository.PatientAdmissionRepository;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.patient_queue.service.PatientQueueService;
import com.pulsecare.backend.module.resource.bed.model.Bed;
import com.pulsecare.backend.module.resource.bed.service.BedService;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import com.pulsecare.backend.module.resource.ward.service.WardService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientAdmissionServiceImpl implements PatientAdmissionService {

    private final PatientAdmissionRepository repository;
    private final PatientAdmissionMapper mapper;
    private final PatientService patientService;
    private final PatientQueueService patientQueueService;
    private final BedService bedService;
    private final WardService wardService;

    public PatientAdmissionServiceImpl(PatientAdmissionRepository repository, @Qualifier("patientAdmissionMapperImpl") PatientAdmissionMapper mapper,
                                       PatientService patientService, PatientQueueService patientQueueService, BedService bedService, WardService wardService) {
        this.repository = repository;
        this.mapper = mapper;
        this.patientService = patientService;
        this.patientQueueService = patientQueueService;
        this.bedService = bedService;
        this.wardService = wardService;
    }

    @Override
    public PatientAdmissionResDTO findById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient admission with id " + id + " not found")));
    }

    @Override
    public PatientAdmission findEntityById(Long id) {
        return repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient admission with id " + id + " not found"));
    }

    @Override
    public List<PatientAdmissionResDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public PatientAdmissionResDTO save(PatientAdmissionReqDTO data) {
        Patient patient = patientService.findEntityById(data.patientId());

        if (repository.existsByPatientIdAndStatus(
                data.patientId(), PatientAdmissionStatus.ACTIVE)) {
            throw new IllegalStateException("Patient already has an active admission");
        }

        PatientQueue queue = null;
        if (data.queueId() != null) {
            queue = patientQueueService.findEntityById(data.queueId());

            if (!queue.getPatient().getId().equals(patient.getId())) {
                throw new IllegalStateException("Queue does not belong to this patient");
            }

            if (queue.getStatus() != QueueStatus.WAITING) {
                throw new IllegalStateException("Patient is not in WAITING queue status");
            }

            queue.setStatus(QueueStatus.ADMITTED);
        }

        Bed bed = bedService.findEntityById(data.bedId());

        PatientAdmission newAdmission = mapper.toEntity(data);

        newAdmission.setPatient(patient);
        newAdmission.setPatientQueue(queue);
        newAdmission.setStatus(PatientAdmissionStatus.ACTIVE);
        newAdmission.setBed(bed);

        bed.setIsTaken(true);

        Ward ward = wardService.findById(bed.getWard().getId());
        int occupiedBeds = ward.getOccupiedBeds() == null ? 0 : ward.getOccupiedBeds();
        ward.setOccupiedBeds(occupiedBeds + 1);

        PatientAdmission saved = repository.save(newAdmission);

        return mapper.toDTO(saved);
    }

    @Override
    public PatientAdmissionResDTO update(Long id, PatientAdmissionReqDTO data) {
//        PatientAdmission existing = findEntityById(id);



        return null;
    }

    @Override
    public void delete(Long id) {

    }

}
