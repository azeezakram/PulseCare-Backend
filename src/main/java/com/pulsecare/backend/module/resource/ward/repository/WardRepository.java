package com.pulsecare.backend.module.resource.ward.repository;

import com.pulsecare.backend.module.resource.ward.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    Optional<Ward> findByNameAndDepartmentId(String name, Integer departmentId);
    Optional<Ward> findWardByWardIdAndDepartmentId(Integer wardId, Integer departmentId);
}
