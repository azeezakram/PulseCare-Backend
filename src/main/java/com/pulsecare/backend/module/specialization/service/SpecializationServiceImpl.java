package com.pulsecare.backend.module.specialization.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.specialization.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;

    public SpecializationServiceImpl(SpecializationRepository repository) {
        this.repository = repository;
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
    public Specialization save(Specialization data) {
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
    public Set<Specialization> findAllById(Set<Integer> ids) {
        List<Specialization> data = repository.findAllById(ids);
        if (data.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more specialization not found");
        }
        return new HashSet<>(data);
    }
}
