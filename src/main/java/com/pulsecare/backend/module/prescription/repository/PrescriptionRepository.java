package com.pulsecare.backend.module.prescription.repository;

import com.pulsecare.backend.module.prescription.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query("select p from Prescription p where p.admission.id = :admissionId")
    List<Prescription> findAllByAdmissionId(@Param("admissionId")Long admissionId);
    List<Prescription> findAllByPatientQueue_IdAndAdmissionIsNull(Long queueId);
}
