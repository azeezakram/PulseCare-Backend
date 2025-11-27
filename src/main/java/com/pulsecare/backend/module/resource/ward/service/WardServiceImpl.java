package com.pulsecare.backend.module.resource.ward.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import com.pulsecare.backend.module.resource.ward.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {

    private final WardRepository repository;

    public WardServiceImpl(WardRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ward findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ward not found with id: " + id));
    }

    @Override
    public List<Ward> findAll() {
        return repository.findAll();
    }

    @Override
    public Ward save(Ward data) {
        return repository.save(data);
    }

    @Override
    public void delete(Integer id) {
        Ward entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ward not found"));

        repository.delete(entity);
    }

    @Override
    public void validateWardNameAndDepartmentIDUniqueness(String wardName, Integer departmentId) {
        repository.findByNameAndDepartmentId(wardName, departmentId)
                .ifPresent(ward -> {
                    throw new ResourceAlreadyExistsException("Ward with this name already exists in the department");

                });
    }

}
