package com.pulsecare.backend.module.patient_admission.repository;

import com.pulsecare.backend.module.patient_admission.enums.PatientAdmissionStatus;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientAdmissionRepository extends JpaRepository<PatientAdmission, Long> {
    boolean existsByPatientIdAndStatus(Long patientId, PatientAdmissionStatus status);

}
