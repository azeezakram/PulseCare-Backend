package com.pulsecare.backend.module.specialization.service;

import com.pulsecare.backend.module.specialization.dto.SpecializationReqDTO;
import com.pulsecare.backend.module.specialization.dto.SpecializationResDTO;
import com.pulsecare.backend.module.specialization.mapper.SpecializationMapper;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.specialization.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;
    private final SpecializationMapper mapper;

    public SpecializationServiceImpl(SpecializationRepository repository, SpecializationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SpecializationResDTO create(SpecializationReqDTO data) {
        return null;
    }

    @Override
    public Byte delete(Integer id) {
        return 0;
    }

    @Override
    public SpecializationResDTO findById(Integer id) {
        return null;
    }

    @Override
    public List<SpecializationResDTO> findAll() {
        List<Specialization> data = repository.findAll();
        return data.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public SpecializationResDTO update(SpecializationReqDTO data) {
        return null;
    }
}
