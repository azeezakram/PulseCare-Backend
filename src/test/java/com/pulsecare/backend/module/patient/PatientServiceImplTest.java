package com.pulsecare.backend.module.patient;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient.dto.PatientReqDTO;
import com.pulsecare.backend.module.patient.dto.PatientResDTO;
import com.pulsecare.backend.module.patient.mapper.PatientMapper;
import com.pulsecare.backend.module.patient.model.Patient;
import com.pulsecare.backend.module.patient.repository.PatientRepository;
import com.pulsecare.backend.module.patient.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock private PatientRepository repository;
    @Mock private PatientMapper mapper;

    @InjectMocks private PatientServiceImpl service;

    private Patient patient;
    private PatientResDTO resDto;

    @BeforeEach
    void setup() {
        patient = new Patient();
        patient.setId(10L);
        patient.setFullName("Abdul Azeez");
        patient.setDob(LocalDate.of(2002, 1, 1));
        patient.setNic("200200200V");
        patient.setGender("MALE");
        patient.setIsActive(true);

        resDto = new PatientResDTO(
                10L,
                "Abdul Azeez",
                LocalDate.of(2002, 1, 1),
                null,
                "200200200V",
                "MALE",
                null,
                true,
                null
        );
    }

    @Test
    void findById_whenExists_returnsDto() {
        when(repository.findById(10L)).thenReturn(Optional.of(patient));
        when(mapper.toDTO(patient)).thenReturn(resDto);

        PatientResDTO result = service.findById(10L);

        assertSame(resDto, result);
        verify(repository).findById(10L);
        verify(mapper).toDTO(patient);
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(10L));

        verify(repository).findById(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_mapsAllToDtos() {
        Patient p2 = new Patient();
        p2.setId(11L);

        PatientResDTO dto2 = mock(PatientResDTO.class);

        when(repository.findAll()).thenReturn(List.of(patient, p2));
        when(mapper.toDTO(patient)).thenReturn(resDto);
        when(mapper.toDTO(p2)).thenReturn(dto2);

        List<PatientResDTO> list = service.findAll();

        assertEquals(2, list.size());
        assertSame(resDto, list.get(0));
        assertSame(dto2, list.get(1));

        verify(repository).findAll();
        verify(mapper).toDTO(patient);
        verify(mapper).toDTO(p2);
    }


    @Test
    void findByIdAndActive_whenExists_returnsDto() {
        when(repository.findByIdAndIsActiveTrue(10L)).thenReturn(Optional.of(patient));
        when(mapper.toDTO(patient)).thenReturn(resDto);

        PatientResDTO result = service.findByIdAndActive(10L);

        assertSame(resDto, result);
        verify(repository).findByIdAndIsActiveTrue(10L);
        verify(mapper).toDTO(patient);
    }

    @Test
    void findByIdAndActive_whenMissing_throwsNotFound() {
        when(repository.findByIdAndIsActiveTrue(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByIdAndActive(10L));

        verify(repository).findByIdAndIsActiveTrue(10L);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAllAndActive_mapsAllToDtos() {
        Patient p2 = new Patient();
        p2.setId(11L);

        PatientResDTO dto2 = mock(PatientResDTO.class);

        when(repository.findAllByIsActiveTrue()).thenReturn(List.of(patient, p2));
        when(mapper.toDTO(patient)).thenReturn(resDto);
        when(mapper.toDTO(p2)).thenReturn(dto2);

        List<PatientResDTO> list = service.findAllAndActive();

        assertEquals(2, list.size());
        verify(repository).findAllByIsActiveTrue();
        verify(mapper).toDTO(patient);
        verify(mapper).toDTO(p2);
    }


    @Test
    void findByNic_whenExists_returnsDto() {
        when(repository.findByNic("200200200V")).thenReturn(Optional.of(patient));
        when(mapper.toDTO(patient)).thenReturn(resDto);

        PatientResDTO result = service.findByNic("200200200V");

        assertSame(resDto, result);
        verify(repository).findByNic("200200200V");
        verify(mapper).toDTO(patient);
    }

    @Test
    void findByNic_whenMissing_throwsNotFound() {
        when(repository.findByNic("X")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByNic("X"));

        verify(repository).findByNic("X");
        verifyNoInteractions(mapper);
    }

    @Test
    void findByNicAndActive_whenExists_returnsDto() {
        when(repository.findByNicAndIsActiveTrue("200200200V")).thenReturn(Optional.of(patient));
        when(mapper.toDTO(patient)).thenReturn(resDto);

        PatientResDTO result = service.findByNicAndActive("200200200V");

        assertSame(resDto, result);
        verify(repository).findByNicAndIsActiveTrue("200200200V");
        verify(mapper).toDTO(patient);
    }


    @Test
    void findByNicAndActive_whenMissing_throwsNotFound() {
        when(repository.findByNicAndIsActiveTrue("X")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByNicAndActive("X"));

        verify(repository).findByNicAndIsActiveTrue("X");
        verifyNoInteractions(mapper);
    }


    @Test
    void findEntityById_whenExists_returnsEntity() {
        when(repository.findById(10L)).thenReturn(Optional.of(patient));

        Patient result = service.findEntityById(10L);

        assertSame(patient, result);
        verify(repository).findById(10L);
    }

    @Test
    void findEntityById_whenMissing_throwsNotFound() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findEntityById(10L));
        verify(repository).findById(10L);
    }

    @Test
    void save_whenNicAlreadyExistsActive_throwsAlreadyExists() {
        PatientReqDTO req = new PatientReqDTO("Name", LocalDate.now(), null, "NIC1", "MALE", null, true);

        when(repository.findByNicAndIsActiveTrue("NIC1")).thenReturn(Optional.of(patient));

        assertThrows(ResourceAlreadyExistsException.class, () -> service.save(req));

        verify(repository).findByNicAndIsActiveTrue("NIC1");
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void save_whenNicNull_skipsUniqCheck_savesMaps_returnsDto() {
        PatientReqDTO req = new PatientReqDTO("Name", LocalDate.now(), null, null, "MALE", null, true);

        Patient entity = new Patient();
        Patient saved = new Patient();
        PatientResDTO dto = mock(PatientResDTO.class);

        when(mapper.toEntity(req)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientResDTO result = service.save(req);

        assertSame(dto, result);

        // no NIC check
        verify(repository, never()).findByNicAndIsActiveTrue(anyString());
        verify(mapper).toEntity(req);
        verify(repository).save(entity);
        verify(mapper).toDTO(saved);
    }

    @Test
    void save_whenNicNotExists_savesMaps_returnsDto() {
        PatientReqDTO req = new PatientReqDTO("Name", LocalDate.now(), null, "NIC2", "MALE", null, true);

        when(repository.findByNicAndIsActiveTrue("NIC2")).thenReturn(Optional.empty());

        Patient entity = new Patient();
        Patient saved = new Patient();
        PatientResDTO dto = mock(PatientResDTO.class);

        when(mapper.toEntity(req)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(dto);

        PatientResDTO result = service.save(req);

        assertSame(dto, result);

        verify(repository).findByNicAndIsActiveTrue("NIC2");
        verify(mapper).toEntity(req);
        verify(repository).save(entity);
        verify(mapper).toDTO(saved);
    }

    @Test
    void update_whenNicTakenByAnotherPatient_throwsAlreadyExists() {
        PatientReqDTO req = new PatientReqDTO("Name", LocalDate.now(), null, "NICX", "MALE", null, true);

        Patient other = new Patient();
        other.setId(99L);

        when(repository.findByNicAndIsActiveTrue("NICX")).thenReturn(Optional.of(other));

        assertThrows(ResourceAlreadyExistsException.class, () -> service.update(10L, req));

        verify(repository).findByNicAndIsActiveTrue("NICX");
        verify(repository, never()).findById(anyLong());
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void update_whenNicBelongsToSamePatient_allowsUpdate_savesReturnsDto() {
        PatientReqDTO req = new PatientReqDTO("New Name", LocalDate.now(), null, "NIC1", "MALE", null, true);

        Patient same = new Patient();
        same.setId(10L);

        when(repository.findByNicAndIsActiveTrue("NIC1")).thenReturn(Optional.of(same));
        when(repository.findById(10L)).thenReturn(Optional.of(patient));

        PatientResDTO dto = mock(PatientResDTO.class);
        when(repository.save(patient)).thenReturn(patient);
        when(mapper.toDTO(patient)).thenReturn(dto);

        PatientResDTO result = service.update(10L, req);

        assertSame(dto, result);

        verify(repository).findByNicAndIsActiveTrue("NIC1");
        verify(repository).findById(10L);
        verify(mapper).updateEntity(req, patient);
        verify(repository).save(patient);
        verify(mapper).toDTO(patient);
    }

    @Test
    void update_whenNicNull_skipsNicCheck_updatesSavesReturnsDto() {
        PatientReqDTO req = new PatientReqDTO("New Name", LocalDate.now(), null, null, "MALE", null, true);

        when(repository.findById(10L)).thenReturn(Optional.of(patient));

        PatientResDTO dto = mock(PatientResDTO.class);
        when(repository.save(patient)).thenReturn(patient);
        when(mapper.toDTO(patient)).thenReturn(dto);

        PatientResDTO result = service.update(10L, req);

        assertSame(dto, result);

        verify(repository, never()).findByNicAndIsActiveTrue(anyString());
        verify(repository).findById(10L);
        verify(mapper).updateEntity(req, patient);
        verify(repository).save(patient);
        verify(mapper).toDTO(patient);
    }

    @Test
    void update_whenPatientMissing_throwsNotFound() {
        PatientReqDTO req = new PatientReqDTO("New Name", LocalDate.now(), null, null, "MALE", null, true);

        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(10L, req));

        verify(repository).findById(10L);
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void delete_whenExists_setsInactiveAndSaves() {
        when(repository.findById(10L)).thenReturn(Optional.of(patient));
        when(repository.save(patient)).thenReturn(patient);

        service.delete(10L);

        assertFalse(patient.getIsActive(), "Patient should be soft-deleted (isActive=false)");
        verify(repository).findById(10L);
        verify(repository).save(patient);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(10L));

        verify(repository).findById(10L);
        verify(repository, never()).save(any());
    }
}
