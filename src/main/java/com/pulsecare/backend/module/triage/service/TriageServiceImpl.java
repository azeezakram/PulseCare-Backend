package com.pulsecare.backend.module.triage.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.triage.model.Triage;
import com.pulsecare.backend.module.triage.repository.TriageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriageServiceImpl implements TriageService {

    private final TriageRepository repository;

    public TriageServiceImpl(TriageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Triage findById(Long id) {
        Triage data = repository.findById(id).orElse(null);
        if (data == null) {
            throw new ResourceNotFoundException("Triage with id " + id + " not found");
        }
        return data;
    }

    @Override
    public List<Triage> findAll() {
        return repository.findAll();
    }

    @Override
    public Triage save(Triage data) {
        return repository.save(data);
    }

    @Override
    public Triage predict(Triage dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }




    @Override
    public Triage update(Long aLong, Triage data) {
        return null;
    }
}
