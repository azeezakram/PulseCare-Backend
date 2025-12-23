package com.pulsecare.backend.module.patient_admission.service;

import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionReqDTO;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionResDTO;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import com.pulsecare.backend.module.patient_admission.repository.PatientAdmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientAdmissionServiceImpl implements PatientAdmissionService {

    private final PatientAdmissionRepository repository;

    public PatientAdmissionServiceImpl(PatientAdmissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public PatientAdmissionResDTO findById(Long id) {
        return null;
    }

    @Override
    public PatientAdmission findEntityById(Long id) {
        return null;
    }

    @Override
    public List<PatientAdmissionResDTO> findAll() {
        return List.of();
    }

    @Override
    public PatientAdmissionResDTO save(PatientAdmissionReqDTO data) {
        return null;
    }

    @Override
    public PatientAdmissionResDTO update(Long aLong, PatientAdmissionReqDTO data) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

}
