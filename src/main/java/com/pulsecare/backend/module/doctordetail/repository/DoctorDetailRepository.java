package com.pulsecare.backend.module.doctordetail.repository;

import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorDetailRepository extends JpaRepository<DoctorDetail, Long> {
    Optional<DoctorDetail> findByUserIdAndLicenseNo(UUID userId, String licenseNo);
    Optional<DoctorDetail> findByUserId(UUID userId);

    // Only check licenseNo if it is being changed
    @Query("SELECT d FROM DoctorDetail d WHERE d.licenseNo = :licenseNo AND d.user.id <> :userId")
    Optional<DoctorDetail> findByLicenseNoAndUserIdNot(@Param("licenseNo") String licenseNo,
                                                       @Param("userId") UUID userId);
}

