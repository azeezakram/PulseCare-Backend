package com.pulsecare.backend.module.doctor_detail.repository;

import com.pulsecare.backend.module.doctor_detail.model.DoctorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorDetailRepository extends JpaRepository<DoctorDetail, Long> {
    Optional<DoctorDetail> findByUserId(UUID userId);
    Optional<DoctorDetail> findByLicenseNo(String licenseNo);
}

