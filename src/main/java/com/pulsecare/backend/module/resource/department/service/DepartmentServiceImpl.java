package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id " + id + " not found"));
    }

    @Override
    public List<Department> findAll() {
        return repository.findAll();
    }

    @Override
    public Department save(Department data) {
        return repository.save(data);
    }

    @Override
    public void delete(Integer id) {
        Department existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department with id " + id + " not found"));

        repository.delete(existing);
    }

    @Override
    public void validateNameUniqueness(String departmentName, Integer departmentId) {
        repository.findByName(departmentName)
                .ifPresent(department -> {
                    if (!department.getId().equals(departmentId)) {
                        throw new ResourceAlreadyExistsException("Department with this name already exists");
                    }
                });
    }

    @Override
    public void validateNameDoesNotExist(String departmentName) {
        repository.findByName(departmentName)
                .ifPresent(department -> {
                    throw new ResourceAlreadyExistsException("Department with name " + departmentName + " already exists");
                });
    }

}
