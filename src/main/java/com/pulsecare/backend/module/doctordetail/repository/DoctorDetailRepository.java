package com.pulsecare.backend.module.doctordetail.repository;

import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDetailRepository extends JpaRepository<DoctorDetail, Long> {
}
