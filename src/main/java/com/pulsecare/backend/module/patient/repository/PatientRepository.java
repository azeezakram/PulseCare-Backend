package com.pulsecare.backend.module.patient.repository;

import com.pulsecare.backend.module.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByNic(String nic);

    Optional<Patient> findByNicAndIsActiveTrue(String nic);
    Optional<Patient> findByIdAndIsActiveTrue(Long id);
    List<Patient> findAllByIsActiveTrue();
}
