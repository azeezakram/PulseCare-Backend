package com.pulsecare.backend.module.patient_admission.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
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
import com.pulsecare.backend.module.prescription.model.Prescription;
import com.pulsecare.backend.module.prescription.repository.PrescriptionRepository;
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
    private final PrescriptionRepository prescriptionRepository;

    public PatientAdmissionServiceImpl(PatientAdmissionRepository repository, @Qualifier("patientAdmissionMapperImpl") PatientAdmissionMapper mapper,
                                       PatientService patientService, PatientQueueService patientQueueService, BedService bedService, WardService wardService, PrescriptionRepository prescriptionRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.patientService = patientService;
        this.patientQueueService = patientQueueService;
        this.bedService = bedService;
        this.wardService = wardService;
        this.prescriptionRepository = prescriptionRepository;
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
            throw new ResourceAlreadyExistsException("Patient already has an active admission");
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

        if (saved.getPatientQueue() != null) {
            Long qid = saved.getPatientQueue().getId();

            List<Prescription> orphanPrescs =
                    prescriptionRepository.findAllByPatientQueue_IdAndAdmissionIsNull(qid);

            for (Prescription p : orphanPrescs) {
                p.setAdmission(saved);
            }
            prescriptionRepository.saveAll(orphanPrescs);
        }

        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public PatientAdmissionResDTO update(Long id, PatientAdmissionReqDTO data) {

        PatientAdmission existing = findEntityById(id);

        if (existing.getStatus() == PatientAdmissionStatus.DISCHARGED && existing.getDischargedAt() != null) {
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

        if (data.status() == PatientAdmissionStatus.DISCHARGED) {
            existing.setStatus(PatientAdmissionStatus.DISCHARGED);
            existing.setDischargeNotes(data.dischargeNotes());

            LocalDateTime dt = data.dischargedAt() != null ? data.dischargedAt() : LocalDateTime.now();
            existing.setDischargedAt(dt);

            Bed bed = existing.getBed();
            bed.setIsTaken(false);

            Ward ward = bed.getWard();
            Integer occupied = ward.getOccupiedBeds();
            ward.setOccupiedBeds(occupied != null && occupied > 0 ? occupied - 1 : 0);
        }

        return mapper.toDTO(repository.save(existing));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        PatientAdmission entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient admission not found"));

        Bed bed = entity.getBed();
        if (bed != null) {
            bed.setIsTaken(false);

            Ward ward = bed.getWard();
            if (ward != null) {
                Integer occupied = ward.getOccupiedBeds();
                ward.setOccupiedBeds(occupied != null && occupied > 0 ? occupied - 1 : 0);
            }
        }

        // if you want to delete prescriptions under this admission:
        // prescriptionRepository.deleteAllByAdmission_Id(id);

        // OR if you want to keep prescriptions but detach:
        List<Prescription> prescs = prescriptionRepository.findAllByAdmissionId(id);
        for (Prescription p : prescs) {
            p.setAdmission(null);
        }
        prescriptionRepository.saveAll(prescs);

        repository.delete(entity);
    }

    @Override
    public Boolean hasActiveAdmission(Long id) {
        patientService.findById(id);
        return repository.existsByPatientIdAndStatus(
                id, PatientAdmissionStatus.ACTIVE);
    }

}
