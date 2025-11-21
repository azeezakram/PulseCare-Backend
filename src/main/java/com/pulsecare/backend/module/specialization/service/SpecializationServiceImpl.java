package com.pulsecare.backend.module.specialization.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.specialization.dto.SpecializationReqDTO;
import com.pulsecare.backend.module.specialization.dto.SpecializationResDTO;
import com.pulsecare.backend.module.specialization.mapper.SpecializationMapper;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.specialization.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;
    private final SpecializationMapper mapper;

    public SpecializationServiceImpl(SpecializationRepository repository, @Qualifier("specializationMapperImpl") SpecializationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SpecializationResDTO findById(Integer id) {
        Specialization data = repository.findById(id).orElse(null);

        return Optional.ofNullable(data)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
    }

    @Override
    public List<SpecializationResDTO> findAll() {
        List<Specialization> data = repository.findAll();
        return data.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public SpecializationResDTO create(SpecializationReqDTO data) {
        repository.findByName(data.name())
                .ifPresent(s -> {
                    throw new ResourceAlreadyExistsException("Specialization with this name already exists");
                });
        Specialization entity = repository.save(mapper.toEntity(data));
        return mapper.toDTO(entity);
    }

    @Override
    public SpecializationResDTO update(Integer id, SpecializationReqDTO data) {
        Specialization existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization with id " + id + " not found"));

        existing.setName(data.name());
        Specialization updated = repository.save(existing);
        return mapper.toDTO(updated);
    }

    @Override
    public void delete(Integer id) {
        Specialization entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        repository.delete(entity);
    }


}
