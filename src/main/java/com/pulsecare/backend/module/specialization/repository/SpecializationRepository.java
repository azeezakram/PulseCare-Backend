package com.pulsecare.backend.module.specialization.repository;

import com.pulsecare.backend.module.specialization.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {
}
