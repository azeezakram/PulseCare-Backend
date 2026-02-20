package com.pulsecare.backend.module.triage.service;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TriageServiceImpl implements TriageService {

    private final TriageRepository repository;
    private final PatientService patientService;
    private final TriageMapper mapper;
    private final MLClientConfig mlClientConfig;
    private final RestTemplate restTemplate;

    public TriageServiceImpl(TriageRepository repository, PatientService patientService, @Qualifier("triageMapperImpl") TriageMapper mapper, MLClientConfig mlClientConfig, RestTemplate restTemplate) {
        this.repository = repository;
        this.patientService = patientService;
        this.mapper = mapper;
        this.mlClientConfig = mlClientConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public TriageResDTO findById(Long id) {
        Triage data = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Triage not found")
        );

        return mapper.toDTO(data);
    }

    @Override
    public Triage findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Triage not found")
                );
    }

    @Override
    public List<TriageResDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public TriageResDTO save(TriageReqDTO data) {
        Triage entity = mapper.toEntity(data);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public TriageResDTO update(Long id, TriageReqDTO data) {
        Triage existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triage not found"));

        mapper.updateEntity(data, existing);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Triage entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triage not found"));

        repository.delete(entity);
    }


    @Override
    public TriageResDTO predict(TriageReqDTO data) {
        Patient patient = patientService.findEntityById(data.patientId());

        TriagePredictionReqDTO dto = mapper.toPredDTOFromReq(data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(mlClientConfig.getApiKeyHeader(), mlClientConfig.getApiKey());

        HttpEntity<TriagePredictionReqDTO> entity =
                new HttpEntity<>(dto, headers);

        ResponseEntity<TriagePredictionResDTO> response =
                restTemplate.exchange(
                        mlClientConfig.getMlApiUrl(),
                        HttpMethod.POST,
                        entity,
                        TriagePredictionResDTO.class
                );
        Triage predicted = mapper.toPredEntity(response.getBody());
        predicted.setPatient(patient);
        return mapper.toDTO(repository.save(predicted));
    }



}
