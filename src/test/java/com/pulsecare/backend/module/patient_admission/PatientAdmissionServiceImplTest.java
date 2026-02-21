package com.pulsecare.backend.module.patient_admission;

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
import com.pulsecare.backend.module.patient_admission.service.PatientAdmissionServiceImpl;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.patient_queue.service.PatientQueueService;
import com.pulsecare.backend.module.prescription.model.Prescription;
import com.pulsecare.backend.module.prescription.repository.PrescriptionRepository;
import com.pulsecare.backend.module.resource.bed.BedService;
import com.pulsecare.backend.module.resource.bed.model.Bed;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import com.pulsecare.backend.module.resource.ward.service.WardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientAdmissionServiceImplTest {

    @Mock
    private PatientAdmissionRepository repository;
    @Mock
    private PatientAdmissionMapper mapper;
    @Mock
    private PatientService patientService;
    @Mock
    private PatientQueueService patientQueueService;
    @Mock
    private BedService bedService;
    @Mock
    private WardService wardService;
    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PatientAdmissionServiceImpl service;

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void findEntityById_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findEntityById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void save_whenActiveAdmissionExists_throwsAlreadyExists() {
        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(10L, null, 5L, null, null, null);

        Patient patient = new Patient();
        patient.setId(10L);

        when(patientService.findEntityById(10L)).thenReturn(patient);
        when(repository.existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.save(req));

        verify(patientService).findEntityById(10L);
        verify(repository).existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE);
        verifyNoInteractions(patientQueueService, bedService, wardService, prescriptionRepository, mapper);
    }

    @Test
    void save_whenQueueBelongsToDifferentPatient_throwsIllegalState() {
        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(10L, 99L, 5L, null, null, null);

        Patient patient = new Patient();
        patient.setId(10L);
        when(patientService.findEntityById(10L)).thenReturn(patient);
        when(repository.existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE)).thenReturn(false);

        PatientQueue queue = new PatientQueue();
        Patient otherPatient = new Patient();
        otherPatient.setId(11L);
        queue.setPatient(otherPatient);
        queue.setStatus(QueueStatus.WAITING);
        queue.setAdmitted(false);

        when(patientQueueService.findEntityById(99L)).thenReturn(queue);

        assertThrows(IllegalStateException.class, () -> service.save(req));

        verify(patientQueueService).findEntityById(99L);
        verifyNoInteractions(bedService, wardService, prescriptionRepository, mapper);
    }

    @Test
    void save_whenQueueNotWaitingAndAdmittedTrue_throwsIllegalState() {
        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(10L, 99L, 5L, null, null, null);

        Patient patient = new Patient();
        patient.setId(10L);
        when(patientService.findEntityById(10L)).thenReturn(patient);
        when(repository.existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE)).thenReturn(false);

        PatientQueue queue = new PatientQueue();
        queue.setId(99L);
        queue.setPatient(patient);
        queue.setStatus(QueueStatus.ADMITTED);
        queue.setAdmitted(true);

        when(patientQueueService.findEntityById(99L)).thenReturn(queue);

        assertThrows(IllegalStateException.class, () -> service.save(req));

        verify(patientQueueService).findEntityById(99L);
        verifyNoInteractions(bedService, wardService, prescriptionRepository, mapper);
    }

    @Test
    void save_whenQueueProvided_setsQueueAdmitted_setsBedTaken_incrementsWardOccupied_linksOrphanPrescriptions() {
        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(10L, 99L, 5L, null, null, null);

        Patient patient = new Patient();
        patient.setId(10L);
        when(patientService.findEntityById(10L)).thenReturn(patient);
        when(repository.existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE)).thenReturn(false);

        PatientQueue queue = new PatientQueue();
        queue.setId(99L);
        queue.setPatient(patient);
        queue.setStatus(QueueStatus.WAITING);
        queue.setAdmitted(false);
        when(patientQueueService.findEntityById(99L)).thenReturn(queue);

        Ward ward = new Ward();
        ward.setId(7);
        ward.setOccupiedBeds(2);

        Bed bed = new Bed();
        bed.setId(5L);
        bed.setIsTaken(false);
        bed.setWard(ward);

        when(bedService.findEntityById(5L)).thenReturn(bed);
        when(wardService.findById(7)).thenReturn(ward);

        PatientAdmission newAdmission = new PatientAdmission();
        when(mapper.toEntity(req)).thenReturn(newAdmission);

        PatientAdmission saved = new PatientAdmission();
        saved.setId(123L);
        saved.setPatientQueue(queue);

        when(repository.save(any(PatientAdmission.class))).thenReturn(saved);

        Prescription pr1 = new Prescription();
        Prescription pr2 = new Prescription();
        when(prescriptionRepository.findAllByPatientQueue_IdAndAdmissionIsNull(99L)).thenReturn(List.of(pr1, pr2));

        PatientAdmissionResDTO out = mock(PatientAdmissionResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(out);

        PatientAdmissionResDTO result = service.save(req);

        assertSame(out, result);
        assertEquals(QueueStatus.ADMITTED, queue.getStatus());
        assertTrue(bed.getIsTaken());
        assertEquals(3, ward.getOccupiedBeds());
        assertSame(saved, pr1.getAdmission());
        assertSame(saved, pr2.getAdmission());

        ArgumentCaptor<PatientAdmission> captor = ArgumentCaptor.forClass(PatientAdmission.class);
        verify(repository).save(captor.capture());
        PatientAdmission arg = captor.getValue();

        assertSame(patient, arg.getPatient());
        assertSame(queue, arg.getPatientQueue());
        assertSame(bed, arg.getBed());
        assertEquals(PatientAdmissionStatus.ACTIVE, arg.getStatus());

        verify(prescriptionRepository).saveAll(List.of(pr1, pr2));
    }

    @Test
    void save_whenQueueNull_doesNotQueryOrphanPrescriptions() {
        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(10L, null, 5L, null, null, null);

        Patient patient = new Patient();
        patient.setId(10L);
        when(patientService.findEntityById(10L)).thenReturn(patient);
        when(repository.existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE)).thenReturn(false);

        Ward ward = new Ward();
        ward.setId(7);
        ward.setOccupiedBeds(0);

        Bed bed = new Bed();
        bed.setId(5L);
        bed.setIsTaken(false);
        bed.setWard(ward);

        when(bedService.findEntityById(5L)).thenReturn(bed);
        when(wardService.findById(7)).thenReturn(ward);

        PatientAdmission newAdmission = new PatientAdmission();
        when(mapper.toEntity(req)).thenReturn(newAdmission);

        PatientAdmission saved = new PatientAdmission();
        saved.setId(123L);
        saved.setPatientQueue(null);

        when(repository.save(any(PatientAdmission.class))).thenReturn(saved);

        PatientAdmissionResDTO out = mock(PatientAdmissionResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(out);

        PatientAdmissionResDTO result = service.save(req);

        assertSame(out, result);
        verify(prescriptionRepository, never()).findAllByPatientQueue_IdAndAdmissionIsNull(anyLong());
        verify(prescriptionRepository, never()).saveAll(anyList());
    }

    @Test
    void update_whenDischargedAlready_throwsIllegalState() {
        PatientAdmission existing = new PatientAdmission();
        existing.setId(1L);
        existing.setStatus(PatientAdmissionStatus.DISCHARGED);
        existing.setDischargedAt(LocalDateTime.now());
        existing.setBed(new Bed());

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(10L, null, null, null, null, null);

        assertThrows(IllegalStateException.class, () -> service.update(1L, req));

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void update_whenBedChange_newBedTaken_throwsIllegalState() {
        Ward ward = new Ward();
        ward.setId(7);
        ward.setOccupiedBeds(2);

        Bed oldBed = new Bed();
        oldBed.setId(5L);
        oldBed.setIsTaken(true);
        oldBed.setWard(ward);

        PatientAdmission existing = new PatientAdmission();
        existing.setId(1L);
        existing.setStatus(PatientAdmissionStatus.ACTIVE);
        existing.setBed(oldBed);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        Bed newBed = new Bed();
        newBed.setId(6L);
        newBed.setIsTaken(true);
        newBed.setWard(ward);

        when(bedService.findEntityById(6L)).thenReturn(newBed);

        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(null, null, 6L, null, null, null);

        assertThrows(IllegalStateException.class, () -> service.update(1L, req));

        verify(bedService).findEntityById(6L);
        verify(repository, never()).save(any());
    }

    @Test
    void update_whenBedChangeSameWard_togglesBedsDoesNotChangeOccupied() {
        Ward ward = new Ward();
        ward.setId(7);
        ward.setOccupiedBeds(2);

        Bed oldBed = new Bed();
        oldBed.setId(5L);
        oldBed.setIsTaken(true);
        oldBed.setWard(ward);

        PatientAdmission existing = new PatientAdmission();
        existing.setId(1L);
        existing.setStatus(PatientAdmissionStatus.ACTIVE);
        existing.setBed(oldBed);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        Bed newBed = new Bed();
        newBed.setId(6L);
        newBed.setIsTaken(false);
        newBed.setWard(ward);

        when(bedService.findEntityById(6L)).thenReturn(newBed);

        PatientAdmission saved = existing;
        when(repository.save(existing)).thenReturn(saved);

        PatientAdmissionResDTO out = mock(PatientAdmissionResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(out);

        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(null, null, 6L, null, null, null);

        PatientAdmissionResDTO result = service.update(1L, req);

        assertSame(out, result);
        assertFalse(oldBed.getIsTaken());
        assertTrue(newBed.getIsTaken());
        assertSame(newBed, existing.getBed());
        assertEquals(2, ward.getOccupiedBeds());
    }

    @Test
    void update_whenBedChangeDifferentWard_adjustsOccupiedBothSides() {
        Ward oldWard = new Ward();
        oldWard.setId(7);
        oldWard.setOccupiedBeds(3);

        Ward newWard = new Ward();
        newWard.setId(8);
        newWard.setOccupiedBeds(1);

        Bed oldBed = new Bed();
        oldBed.setId(5L);
        oldBed.setIsTaken(true);
        oldBed.setWard(oldWard);

        PatientAdmission existing = new PatientAdmission();
        existing.setId(1L);
        existing.setStatus(PatientAdmissionStatus.ACTIVE);
        existing.setBed(oldBed);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        Bed newBed = new Bed();
        newBed.setId(6L);
        newBed.setIsTaken(false);
        newBed.setWard(newWard);

        when(bedService.findEntityById(6L)).thenReturn(newBed);

        when(repository.save(existing)).thenReturn(existing);

        PatientAdmissionResDTO out = mock(PatientAdmissionResDTO.class);
        when(mapper.toDTO(existing)).thenReturn(out);

        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(null, null, 6L, null, null, null);

        PatientAdmissionResDTO result = service.update(1L, req);

        assertSame(out, result);
        assertFalse(oldBed.getIsTaken());
        assertTrue(newBed.getIsTaken());
        assertSame(newBed, existing.getBed());
        assertEquals(2, oldWard.getOccupiedBeds());
        assertEquals(2, newWard.getOccupiedBeds());
    }

    @Test
    void update_whenDischarge_setsDischarged_setsDate_defaultsNow_freesBed_decrementsWard() {
        Ward ward = new Ward();
        ward.setId(7);
        ward.setOccupiedBeds(1);

        Bed bed = new Bed();
        bed.setId(5L);
        bed.setIsTaken(true);
        bed.setWard(ward);

        PatientAdmission existing = new PatientAdmission();
        existing.setId(1L);
        existing.setStatus(PatientAdmissionStatus.ACTIVE);
        existing.setBed(bed);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        PatientAdmissionResDTO out = mock(PatientAdmissionResDTO.class);
        when(mapper.toDTO(existing)).thenReturn(out);

        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(
                null, null, null, PatientAdmissionStatus.DISCHARGED, "done", null
        );

        PatientAdmissionResDTO result = service.update(1L, req);

        assertSame(out, result);
        assertEquals(PatientAdmissionStatus.DISCHARGED, existing.getStatus());
        assertEquals("done", existing.getDischargeNotes());
        assertNotNull(existing.getDischargedAt());
        assertFalse(bed.getIsTaken());
        assertEquals(0, ward.getOccupiedBeds());
    }

    @Test
    void update_whenDischarge_withProvidedDate_usesProvidedDate() {
        Ward ward = new Ward();
        ward.setOccupiedBeds(2);

        Bed bed = new Bed();
        bed.setIsTaken(true);
        bed.setWard(ward);

        PatientAdmission existing = new PatientAdmission();
        existing.setId(1L);
        existing.setStatus(PatientAdmissionStatus.ACTIVE);
        existing.setBed(bed);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        PatientAdmissionResDTO out = mock(PatientAdmissionResDTO.class);
        when(mapper.toDTO(existing)).thenReturn(out);

        LocalDateTime dt = LocalDateTime.of(2026, 2, 21, 10, 0);

        PatientAdmissionReqDTO req = new PatientAdmissionReqDTO(
                null, null, null, PatientAdmissionStatus.DISCHARGED, "ok", dt
        );

        PatientAdmissionResDTO result = service.update(1L, req);

        assertSame(out, result);
        assertEquals(dt, existing.getDischargedAt());
        assertEquals(1, ward.getOccupiedBeds());
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
        verify(repository).findById(1L);
    }

    @Test
    void delete_whenExists_freesBed_decrementsWard_detachesPrescriptions_deletesAdmission() {
        Ward ward = new Ward();
        ward.setOccupiedBeds(2);

        Bed bed = new Bed();
        bed.setIsTaken(true);
        bed.setWard(ward);

        PatientAdmission entity = new PatientAdmission();
        entity.setId(1L);
        entity.setBed(bed);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Prescription p1 = new Prescription();
        Prescription p2 = new Prescription();
        p1.setAdmission(entity);
        p2.setAdmission(entity);

        when(prescriptionRepository.findAllByAdmissionId(1L)).thenReturn(List.of(p1, p2));

        service.delete(1L);

        assertFalse(bed.getIsTaken());
        assertEquals(1, ward.getOccupiedBeds());
        assertNull(p1.getAdmission());
        assertNull(p2.getAdmission());

        verify(prescriptionRepository).saveAll(List.of(p1, p2));
        verify(repository).delete(entity);
    }

    @Test
    void hasActiveAdmission_callsPatientServiceFindById_andReturnsRepositoryExists() {
        when(repository.existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE)).thenReturn(true);

        service.hasActiveAdmission(10L);

        verify(patientService).findById(10L);
        verify(repository).existsByPatientIdAndStatus(10L, PatientAdmissionStatus.ACTIVE);
    }
}