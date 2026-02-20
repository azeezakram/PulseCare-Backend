package com.pulsecare.backend.module.patient.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient.dto.PatientReqDTO;
import com.pulsecare.backend.module.patient.dto.PatientResDTO;
import com.pulsecare.backend.module.patient.mapper.PatientMapper;
import com.pulsecare.backend.module.patient.model.Patient;
import com.pulsecare.backend.module.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    public PatientServiceImpl(PatientRepository repository, @Qualifier("patientMapperImpl") PatientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PatientResDTO findById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient not found")));
    }

    @Override
    public List<PatientResDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public PatientResDTO findByIdAndActive(Long id) {
        return mapper.toDTO(
                repository.findByIdAndIsActiveTrue(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient not found")));
    }

    @Override
    public List<PatientResDTO> findAllAndActive() {
        return repository.findAllByIsActiveTrue().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public PatientResDTO findByNic(String nic) {
        return mapper.toDTO(
                repository.findByNic(nic)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient with this NIC not found")));
    }

    @Override
    public PatientResDTO findByNicAndActive(String nic) {
        return mapper.toDTO(
                repository.findByNicAndIsActiveTrue(nic)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient with this NIC not found")));
    }

    @Override
    public Patient findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Patient not found"));
    }


    @Override
    @Transactional
    public PatientResDTO save(PatientReqDTO data) {

        if (data.nic() != null && repository.findByNicAndIsActiveTrue(data.nic()).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Patient with NIC " + data.nic() + " already exists"
            );
        }

        Patient saved = repository.save(mapper.toEntity(data));

        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public PatientResDTO update(Long id, PatientReqDTO data) {

        if (data.nic() != null) {
            Patient byNic = repository.findByNicAndIsActiveTrue(data.nic()).orElse(null);
            if (byNic != null && !byNic.getId().equals(id)) {
                throw new ResourceAlreadyExistsException(
                        "Patient with NIC " + data.nic() + " already exists"
                );
            }
        }

        Patient existing = findEntityById(id);

        mapper.updateEntity(data, existing);

        Patient updated = repository.save(existing);

        return mapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Patient entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        entity.setIsActive(false);
        repository.save(entity);
    }


}
