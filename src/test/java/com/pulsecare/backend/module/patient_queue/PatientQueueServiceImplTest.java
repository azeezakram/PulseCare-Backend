package com.pulsecare.backend.module.patient_queue;

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
import com.pulsecare.backend.module.patient_queue.service.PatientQueueServiceImpl;
import com.pulsecare.backend.module.patient_queue.ws.PatientQueueEvent;
import com.pulsecare.backend.module.patient_queue.ws.PatientQueueEventPublisher;
import com.pulsecare.backend.module.triage.model.Triage;
import com.pulsecare.backend.module.triage.service.TriageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientQueueServiceImplTest {

    @Mock
    private PatientQueueRepository repository;
    @Mock
    private PatientQueueMapper mapper;
    @Mock
    private TriageService triageService;
    @Mock
    private PatientService patientService;
    @Mock
    private PatientAdmissionService patientAdmissionService;
    @Mock
    private PatientQueueEventPublisher publisher;

    @InjectMocks
    private PatientQueueServiceImpl service;

    @Test
    void save_whenPatientIdNull_throwsValidation() {
        PatientQueueReqDTO req = new PatientQueueReqDTO(
                null, null, QueuePriority.NORMAL, false, null
        );

        assertThrows(ValidationException.class, () -> service.save(req));
        verifyNoInteractions(repository, mapper, triageService, patientService, patientAdmissionService, publisher);
    }

    @Test
    void save_whenHasActiveAdmission_throwsAlreadyExists() {
        PatientQueueReqDTO req = new PatientQueueReqDTO(
                10L, null, QueuePriority.NORMAL, false, null
        );

        when(patientAdmissionService.hasActiveAdmission(10L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.save(req));

        verify(patientAdmissionService).hasActiveAdmission(10L);
        verifyNoMoreInteractions(patientAdmissionService);
        verifyNoInteractions(repository, mapper, triageService, patientService, publisher);
    }

    @Test
    void save_whenTriageProvided_setsPriorityFromTriage_setsPatient_setsWaiting_saves_publishesCreated() {
        PatientQueueReqDTO req = new PatientQueueReqDTO(
                10L, 5L, QueuePriority.NORMAL, false, null
        );

        when(patientAdmissionService.hasActiveAdmission(10L)).thenReturn(false);

        PatientQueue entity = new PatientQueue();
        when(mapper.toEntity(req)).thenReturn(entity);

        Triage triage = new Triage();
        triage.setId(5L);
        triage.setTriageLevel(0); // critical
        when(triageService.findEntityById(5L)).thenReturn(triage);

        Patient patient = new Patient();
        patient.setId(10L);
        when(patientService.findEntityById(10L)).thenReturn(patient);

        PatientQueue saved = new PatientQueue();
        when(repository.save(entity)).thenReturn(saved);

        PatientQueueResDTO dto = mock(PatientQueueResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientQueueResDTO result = service.save(req);

        assertSame(dto, result);
        assertSame(triage, entity.getTriage());
        assertSame(patient, entity.getPatient());
        assertEquals(QueuePriority.CRITICAL, entity.getPriority());
        assertEquals(QueueStatus.WAITING, entity.getStatus());

        verify(patientAdmissionService).hasActiveAdmission(10L);
        verify(mapper).toEntity(req);
        verify(triageService).findEntityById(5L);
        verify(patientService).findEntityById(10L);
        verify(repository).save(entity);
        verify(mapper).toDTO(saved);

        // Event creation is static: we just verify publisher.publish(...) is called
        verify(publisher).publish(any(PatientQueueEvent.class));
    }

    @Test
    void save_whenNoTriage_usesProvidedPriority_setsWaiting_saves_publishesCreated() {
        PatientQueueReqDTO req = new PatientQueueReqDTO(
                10L, null, QueuePriority.NON_CRITICAL, false, null
        );

        when(patientAdmissionService.hasActiveAdmission(10L)).thenReturn(false);

        PatientQueue entity = new PatientQueue();
        when(mapper.toEntity(req)).thenReturn(entity);

        Patient patient = new Patient();
        patient.setId(10L);
        when(patientService.findEntityById(10L)).thenReturn(patient);

        PatientQueue saved = new PatientQueue();
        when(repository.save(entity)).thenReturn(saved);

        PatientQueueResDTO dto = mock(PatientQueueResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientQueueResDTO result = service.save(req);

        assertSame(dto, result);
        assertSame(patient, entity.getPatient());
        assertEquals(QueuePriority.NON_CRITICAL, entity.getPriority());
        assertEquals(QueueStatus.WAITING, entity.getStatus());

        verify(patientAdmissionService).hasActiveAdmission(10L);
        verify(mapper).toEntity(req);
        verify(patientService).findEntityById(10L);
        verify(repository).save(entity);
        verify(mapper).toDTO(saved);
        verify(publisher).publish(any(PatientQueueEvent.class));
        verifyNoInteractions(triageService);
    }

    @Test
    void update_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(1L, new PatientQueueReqDTO(null, null, null, null, null)));

        verify(repository).findById(1L);
        verifyNoInteractions(mapper, triageService, patientService, publisher);
    }

    @Test
    void update_whenTriageProvided_setsPriorityFromTriage_saves_publishesUpdated() {
        PatientQueue existing = new PatientQueue();
        existing.setStatus(QueueStatus.WAITING);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        PatientQueueReqDTO req = new PatientQueueReqDTO(
                null, 5L, null, null, null
        );

        Triage triage = new Triage();
        triage.setId(5L);
        triage.setTriageLevel(1);
        when(triageService.findEntityById(5L)).thenReturn(triage);

        PatientQueue saved = new PatientQueue();
        when(repository.save(existing)).thenReturn(saved);

        PatientQueueResDTO dto = mock(PatientQueueResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientQueueResDTO result = service.update(1L, req);

        assertSame(dto, result);
        assertSame(triage, existing.getTriage());
        assertEquals(QueuePriority.NON_CRITICAL, existing.getPriority());

        verify(repository).findById(1L);
        verify(mapper).updateEntity(req, existing);
        verify(triageService).findEntityById(5L);
        verify(repository).save(existing);
        verify(mapper).toDTO(saved);
        verify(publisher).publish(any(PatientQueueEvent.class));
    }

    @Test
    void update_whenPriorityProvidedWithoutTriage_setsPriority_saves_publishesUpdated() {
        PatientQueue existing = new PatientQueue();
        existing.setStatus(QueueStatus.WAITING);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        PatientQueueReqDTO req = new PatientQueueReqDTO(
                null, null, QueuePriority.NORMAL, null, null
        );

        PatientQueue saved = new PatientQueue();
        when(repository.save(existing)).thenReturn(saved);

        PatientQueueResDTO dto = mock(PatientQueueResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientQueueResDTO result = service.update(1L, req);

        assertSame(dto, result);
        assertEquals(QueuePriority.NORMAL, existing.getPriority());

        verify(repository).findById(1L);
        verify(mapper).updateEntity(req, existing);
        verify(repository).save(existing);
        verify(mapper).toDTO(saved);
        verify(publisher).publish(any(PatientQueueEvent.class));
        verifyNoInteractions(triageService);
    }

    @Test
    void update_whenPatientIdProvided_setsPatient_saves_publishesUpdated() {
        PatientQueue existing = new PatientQueue();
        existing.setStatus(QueueStatus.WAITING);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        PatientQueueReqDTO req = new PatientQueueReqDTO(
                10L, null, null, null, null
        );

        Patient patient = new Patient();
        patient.setId(10L);
        when(patientService.findEntityById(10L)).thenReturn(patient);

        PatientQueue saved = new PatientQueue();
        when(repository.save(existing)).thenReturn(saved);

        PatientQueueResDTO dto = mock(PatientQueueResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientQueueResDTO result = service.update(1L, req);

        assertSame(dto, result);
        assertSame(patient, existing.getPatient());

        verify(repository).findById(1L);
        verify(mapper).updateEntity(req, existing);
        verify(patientService).findEntityById(10L);
        verify(repository).save(existing);
        verify(mapper).toDTO(saved);
        verify(publisher).publish(any(PatientQueueEvent.class));
    }

    @Test
    void update_whenStatusProvided_validTransition_setsStatus_saves_publishesUpdated() {
        PatientQueue existing = new PatientQueue();
        existing.setStatus(QueueStatus.WAITING);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        PatientQueueReqDTO req = new PatientQueueReqDTO(
                null, null, null, null, QueueStatus.ADMITTED
        );

        PatientQueue saved = new PatientQueue();
        when(repository.save(existing)).thenReturn(saved);

        PatientQueueResDTO dto = mock(PatientQueueResDTO.class);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientQueueResDTO result = service.update(1L, req);

        assertSame(dto, result);
        assertEquals(QueueStatus.ADMITTED, existing.getStatus());

        verify(repository).findById(1L);
        verify(mapper).updateEntity(req, existing);
        verify(repository).save(existing);
        verify(mapper).toDTO(saved);
        verify(publisher).publish(any(PatientQueueEvent.class));
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));

        verify(repository).findById(1L);
        verify(repository, never()).delete(any());
        verifyNoInteractions(publisher);
    }

    @Test
    void delete_whenExists_deletes_publishesDeleted() {
        PatientQueue existing = new PatientQueue();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repository).findById(1L);
        verify(repository).delete(existing);
        verify(publisher).publish(any(PatientQueueEvent.class));
    }
}