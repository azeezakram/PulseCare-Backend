package com.pulsecare.backend.module.triage.service;

import com.pulsecare.backend.module.triage.model.Triage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriageServiceImpl implements TriageService {

    @Override
    public Triage predict(Triage dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Triage findById(Long id) {
        return null;
    }

    @Override
    public List<Triage> findAll() {
        return List.of();
    }

    @Override
    public Triage save(Triage data) {
        return null;
    }

    @Override
    public Triage update(Long aLong, Triage data) {
        return null;
    }
}
