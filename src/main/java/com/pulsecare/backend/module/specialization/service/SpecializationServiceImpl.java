package com.pulsecare.backend.module.specialization.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;
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
    public Specialization findById(Integer id) {
        Specialization data = repository.findById(id).orElse(null);
        if (data == null) {
            throw new ResourceNotFoundException("Specialization with id " + id + " not found");
        }
        return data;
    }

    @Override
    public List<Specialization> findAll() {
        return repository.findAll();
    }

    @Override
    public Specialization create(Specialization data) {
        repository.findByName(data.getName())
                .ifPresent(s -> {
                    throw new ResourceAlreadyExistsException("Specialization with this name already exists");
                });
        return repository.save(data);
    }

    @Override
    public Specialization update(Integer id, Specialization data) {
        Specialization existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization with id " + id + " not found"));

        existing.setName(data.getName());
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        Specialization entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        repository.delete(entity);
    }


    @Override
    public List<Specialization> findAllById(List<Integer> ids) {
        return repository.findAllById(ids);
    }
}
