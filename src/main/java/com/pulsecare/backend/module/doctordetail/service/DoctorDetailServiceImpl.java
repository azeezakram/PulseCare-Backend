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
        DoctorDetail data = repository.findById(id).orElse(null);
        if (data == null) {
            throw new ResourceNotFoundException("Doctor detail with id " + id + " not found");
        }
        return data;
    }

    @Override
    public List<DoctorDetail> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public DoctorDetail create(DoctorDetail data) {
        repository.findByUserIdAndLicenseNo(data.getUser().getId(), data.getLicenseNo())
                .ifPresent(s -> {
                    throw new ResourceAlreadyExistsException(
                            "Doctor detail with this User ID or License No already exists");
                });

        return repository.save(data);
    }

    @Override
    public DoctorDetail update(Long id, DoctorDetail data) {
        DoctorDetail existingDetail = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor detail with id " + id + " not found"));

        // Only check licenseNo if it is being changed
        if (data.getLicenseNo() != null && !data.getLicenseNo().equals(existingDetail.getLicenseNo())) {
            repository.findByLicenseNoAndUserIdNot(data.getLicenseNo(), existingDetail.getUser().getId())
                    .ifPresent(d -> {
                        throw new ResourceAlreadyExistsException(
                                "License number already taken by another doctor"
                        );
                    });
            existingDetail.setLicenseNo(data.getLicenseNo());
        }

        if (data.getSpecializations() != null) {
            existingDetail.setSpecializations(data.getSpecializations());
        }

        return repository.save(existingDetail);
    }

    @Override
    public DoctorDetail update(String userId, DoctorDetail data) {
        UUID userUUID = UUID.fromString(userId);

        // Fetch existing doctor detail for the user
        DoctorDetail existingDetail = repository.findByUserId(userUUID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor detail with user id " + userId + " not found"
                ));

        // Only check licenseNo if it is being changed
        if (data.getLicenseNo() != null && !data.getLicenseNo().equals(existingDetail.getLicenseNo())) {
            repository.findByLicenseNoAndUserIdNot(data.getLicenseNo(), userUUID)
                    .ifPresent(d -> {
                        throw new ResourceAlreadyExistsException(
                                "License number already taken by another doctor"
                        );
                    });
        }

        if (data.getLicenseNo() != null) {
            existingDetail.setLicenseNo(data.getLicenseNo());
        }

        if (data.getSpecializations() != null) {
            existingDetail.setSpecializations(data.getSpecializations());
        }

        return repository.save(existingDetail);

    }


    @Override
    public void delete(Long id) {
        DoctorDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor detail not found"));

        repository.delete(entity);
    }

    @Override
    public DoctorDetail findByUserId(String userId) {
        DoctorDetail data = repository.findByUserId(UUID.fromString(userId)).orElse(null);
        if (data == null) {
            throw new ResourceNotFoundException("Doctor detail with user id " + userId + " not found");
        }
        return data;
    }
}
