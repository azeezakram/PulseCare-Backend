package com.pulsecare.backend.module.triage.repository;

import com.pulsecare.backend.module.triage.model.Triage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageRepository extends JpaRepository<Triage, Long> {
}
