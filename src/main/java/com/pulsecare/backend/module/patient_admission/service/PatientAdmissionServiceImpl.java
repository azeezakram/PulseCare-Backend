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

import java.time.LocalDateTime;
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

            if (queue.getStatus() != QueueStatus.WAITING && queue.getAdmitted().equals(true)) {
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
    @Transactional
    public PatientAdmissionResDTO update(Long id, PatientAdmissionReqDTO data) {

        PatientAdmission existing = findEntityById(id);

        if (existing.getStatus() == PatientAdmissionStatus.DISCHARGED) {
            throw new IllegalStateException("Cannot update a discharged admission");
        }

        // Bed change
        if (data.bedId() != null &&
                !data.bedId().equals(existing.getBed().getId())) {

            Bed newBed = bedService.findEntityById(data.bedId());

            if (Boolean.TRUE.equals(newBed.getIsTaken())) {
                throw new IllegalStateException("Selected bed is already occupied");
            }

            Bed oldBed = existing.getBed();
            Ward oldWard = oldBed.getWard();
            Ward newWard = newBed.getWard();

            oldBed.setIsTaken(false);
            newBed.setIsTaken(true);
            existing.setBed(newBed);

            if (!oldWard.getId().equals(newWard.getId())) {
                oldWard.setOccupiedBeds(oldWard.getOccupiedBeds() - 1);
                newWard.setOccupiedBeds(
                        (newWard.getOccupiedBeds() == null ? 0 : newWard.getOccupiedBeds()) + 1
                );
            }
        }

        // Discharge
        if (data.status() == PatientAdmissionStatus.DISCHARGED) {

            existing.setStatus(PatientAdmissionStatus.DISCHARGED);
            existing.setDischargedAt(LocalDateTime.now());
            existing.setDischargeNotes(data.dischargeNotes());

            Bed bed = existing.getBed();
            bed.setIsTaken(false);

            Ward ward = bed.getWard();
            Integer occupied = ward.getOccupiedBeds();
            ward.setOccupiedBeds(
                    occupied != null && occupied > 0 ? occupied - 1 : 0
            );
        }

        return mapper.toDTO(repository.save(existing));
    }


    @Override
    public void delete(Long id) {
        PatientAdmission entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient admission not found"));

        repository.delete(entity);
    }

}
