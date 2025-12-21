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
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient with id " + id + " not found")));
    }

    @Override
    public List<PatientResDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Patient findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Patient with id " + id + " not found"));
    }

    @Override
    public PatientResDTO save(PatientReqDTO data) {

        if (data.nic() != null && repository.findByNic(data.nic()) != null) {
            throw new ResourceAlreadyExistsException(
                    "Patient with NIC " + data.nic() + " already exists"
            );
        }

        Patient saved = repository.save(mapper.toEntity(data));

        return mapper.toDTO(saved);
    }

    @Override
    public PatientResDTO update(Long id, PatientReqDTO data) {

        Patient existing = findEntityById(id);

        if (data.nic() != null) {
            Patient byNic = repository.findByNic(data.nic());
            if (byNic != null && !byNic.getId().equals(id)) {
                throw new ResourceAlreadyExistsException(
                        "Patient with NIC " + data.nic() + " already exists"
                );
            }
        }

        mapper.updateEntity(data, existing);

        Patient updated = repository.save(existing);

        return mapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Patient entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        repository.delete(entity);
    }


}
