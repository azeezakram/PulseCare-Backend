package com.pulsecare.backend.module.prescription.repository;

import com.pulsecare.backend.module.prescription.model.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {
    @Query("select pt from PrescriptionItem pt where pt.prescription.id = :prescriptionId")
    List<PrescriptionItem> findAllByPrescriptionId(@Param("prescriptionId") Long prescriptionId);

}
