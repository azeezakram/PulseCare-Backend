package com.pulsecare.backend.module.triage;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient.model.Patient;
import com.pulsecare.backend.module.patient.service.PatientService;
import com.pulsecare.backend.module.triage.config.MLClientConfig;
import com.pulsecare.backend.module.triage.dto.TriagePredictionReqDTO;
import com.pulsecare.backend.module.triage.dto.TriagePredictionResDTO;
import com.pulsecare.backend.module.triage.dto.TriageReqDTO;
import com.pulsecare.backend.module.triage.dto.TriageResDTO;
import com.pulsecare.backend.module.triage.mapper.TriageMapper;
import com.pulsecare.backend.module.triage.model.Triage;
import com.pulsecare.backend.module.triage.repository.TriageRepository;
import com.pulsecare.backend.module.triage.service.TriageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriageServiceImplTest {

    @Mock
    private TriageRepository repository;
    @Mock
    private PatientService patientService;
    @Mock
    private TriageMapper mapper;
    @Mock
    private MLClientConfig mlClientConfig;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TriageServiceImpl service;

    private TriageReqDTO sampleReq() {
        return new TriageReqDTO(
                10L,  // patientId
                1,    // sex
                2,    // arrivalMode
                2,    // injury
                1,    // mental
                0,    // pain
                25,   // age
                120,  // sbp
                80,   // dbp
                90,   // hr
                18,   // rr
                36.8  // bt
        );
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void findById_whenExists_mapsToDto() {
        Triage triage = new Triage();
        TriageResDTO dto = mock(TriageResDTO.class);

        when(repository.findById(1L)).thenReturn(Optional.of(triage));
        when(mapper.toDTO(triage)).thenReturn(dto);

        TriageResDTO result = service.findById(1L);

        assertSame(dto, result);
        verify(repository).findById(1L);
        verify(mapper).toDTO(triage);
    }

    @Test
    void findEntityById_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findEntityById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void findAll_mapsAllToDtos() {
        Triage t1 = new Triage();
        Triage t2 = new Triage();
        TriageResDTO d1 = mock(TriageResDTO.class);
        TriageResDTO d2 = mock(TriageResDTO.class);

        when(repository.findAll()).thenReturn(List.of(t1, t2));
        when(mapper.toDTO(t1)).thenReturn(d1);
        when(mapper.toDTO(t2)).thenReturn(d2);

        List<TriageResDTO> result = service.findAll();

        assertEquals(2, result.size());
        assertSame(d1, result.get(0));
        assertSame(d2, result.get(1));

        verify(repository).findAll();
        verify(mapper).toDTO(t1);
        verify(mapper).toDTO(t2);
    }

    @Test
    void save_maps_saves_mapsBack() {
        TriageReqDTO req = sampleReq();
        Triage entity = new Triage();
        Triage saved = new Triage();
        TriageResDTO dto = mock(TriageResDTO.class);

        when(mapper.toEntity(req)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(dto);

        TriageResDTO result = service.save(req);

        assertSame(dto, result);
        verify(mapper).toEntity(req);
        verify(repository).save(entity);
        verify(mapper).toDTO(saved);
    }


    @Test
    void update_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, sampleReq()));

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void update_whenExists_updates_saves_mapsBack() {
        TriageReqDTO req = sampleReq();
        Triage existing = new Triage();
        Triage saved = new Triage();
        TriageResDTO dto = mock(TriageResDTO.class);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(dto);

        TriageResDTO result = service.update(1L, req);

        assertSame(dto, result);

        verify(repository).findById(1L);
        verify(mapper).updateEntity(req, existing);
        verify(repository).save(existing);
        verify(mapper).toDTO(saved);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));

        verify(repository).findById(1L);
        verify(repository, never()).delete(any());
    }

    @Test
    void delete_whenExists_deletes() {
        Triage existing = new Triage();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repository).findById(1L);
        verify(repository).delete(existing);
    }


    @Test
    void predict_success_callsMl_setsHeaders_setsPatient_saves_mapsBack() {
        TriageReqDTO req = sampleReq();

        Patient patient = new Patient();
        patient.setId(10L);

        TriagePredictionReqDTO predReq = new TriagePredictionReqDTO(
                req.sex(), req.arrivalMode(), req.injury(), req.mental(), req.pain(),
                req.age(), req.sbp(), req.dbp(), req.hr(), req.rr(), req.bt()
        );

        TriagePredictionResDTO predRes = new TriagePredictionResDTO(
                0, 0.91, "CRITICAL",
                req.sex(), req.arrivalMode(), req.injury(), req.mental(), req.pain(),
                req.age(), req.sbp(), req.dbp(), req.hr(), req.rr(), req.bt(),
                0.75, 40.0, 0.33, 3312.0, 0.2,
                true, true, false, false, false
        );

        // This is what you will save after mapping prediction response
        Triage predictedEntity = new Triage();
        Triage saved = new Triage();
        TriageResDTO outDto = mock(TriageResDTO.class);

        when(patientService.findEntityById(10L)).thenReturn(patient);
        when(mapper.toPredDTOFromReq(req)).thenReturn(predReq);

        when(mlClientConfig.getApiKeyHeader()).thenReturn("X-API-KEY");
        when(mlClientConfig.getApiKey()).thenReturn("SECRET");
        when(mlClientConfig.getMlApiUrl()).thenReturn("http://ml/predict");

        ResponseEntity<TriagePredictionResDTO> responseEntity = ResponseEntity.ok(predRes);

        // Capture the HttpEntity to validate headers/body
        ArgumentCaptor<HttpEntity<TriagePredictionReqDTO>> httpEntityCaptor =
                ArgumentCaptor.forClass((Class) HttpEntity.class);

        when(restTemplate.exchange(
                eq("http://ml/predict"),
                eq(HttpMethod.POST),
                httpEntityCaptor.capture(),
                eq(TriagePredictionResDTO.class)
        )).thenReturn(responseEntity);

        when(mapper.toPredEntity(predRes)).thenReturn(predictedEntity);
        when(repository.save(predictedEntity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(outDto);

        TriageResDTO result = service.predict(req);

        assertSame(outDto, result);

        // verify headers + body were created correctly
        HttpEntity<TriagePredictionReqDTO> sentEntity = httpEntityCaptor.getValue();
        assertNotNull(sentEntity);

        assertEquals(MediaType.APPLICATION_JSON, sentEntity.getHeaders().getContentType());
        assertEquals("SECRET", sentEntity.getHeaders().getFirst("X-API-KEY"));
        assertEquals(predReq, sentEntity.getBody());

        // verify patient is attached to predicted entity before save
        assertSame(patient, predictedEntity.getPatient());

        verify(patientService).findEntityById(10L);
        verify(mapper).toPredDTOFromReq(req);
        verify(restTemplate).exchange(eq("http://ml/predict"), eq(HttpMethod.POST), any(HttpEntity.class), eq(TriagePredictionResDTO.class));
        verify(mapper).toPredEntity(predRes);
        verify(repository).save(predictedEntity);
        verify(mapper).toDTO(saved);
    }

    @Test
    void predict_whenPatientMissing_throwsNotFound_andDoesNotCallMl() {
        TriageReqDTO req = sampleReq();

        when(patientService.findEntityById(10L))
                .thenThrow(new ResourceNotFoundException("Patient not found"));

        assertThrows(ResourceNotFoundException.class, () -> service.predict(req));

        verify(patientService).findEntityById(10L);
        verifyNoInteractions(restTemplate, mlClientConfig, mapper, repository);
    }
}