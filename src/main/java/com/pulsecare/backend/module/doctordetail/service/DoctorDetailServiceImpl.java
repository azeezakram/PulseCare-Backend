package com.pulsecare.backend.module.doctordetail.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;
import com.pulsecare.backend.module.doctordetail.repository.DoctorDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorDetailServiceImpl implements DoctorDetailService {

    private final DoctorDetailRepository repository;

    public DoctorDetailServiceImpl(DoctorDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public DoctorDetail findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Doctor detail with id " + id + " not found")
        );
    }

    @Override
    public DoctorDetail findByUserId(String userId) {
        return repository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor detail with user id " + userId + " not found")
                );
    }

    @Override
    public List<DoctorDetail> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public DoctorDetail save(DoctorDetail data) {
        return repository.save(data);
    }

    @Override
    public void validateLicenseNoUniqueness(String licenseNo, UUID userId) {
        repository.findByLicenseNo(licenseNo)
                .ifPresent(d -> {
                    if (!d.getUser().getId().equals(userId)) {
                        throw new ResourceAlreadyExistsException(
                                "License number already taken by another doctor"
                        );
                    }
                });
    }

    @Override
    public void validateAlreadyHasDoctorDetail(UUID userId) {
        repository.findByUserId(userId)
                .ifPresent(d -> {
                    throw new ResourceAlreadyExistsException(
                            "This user already has a doctor detail profile"
                    );
                });
    }

    @Override
    public void delete(Long id) {
        DoctorDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor detail not found"));

        repository.delete(entity);
    }

}
