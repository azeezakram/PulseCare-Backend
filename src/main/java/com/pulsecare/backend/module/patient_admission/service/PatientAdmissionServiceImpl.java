package com.pulsecare.backend.module.patient_admission.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient.service.PatientService;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionReqDTO;
import com.pulsecare.backend.module.patient_admission.dto.PatientAdmissionResDTO;
import com.pulsecare.backend.module.patient_admission.mapper.PatientAdmissionMapper;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import com.pulsecare.backend.module.patient_admission.repository.PatientAdmissionRepository;
import com.pulsecare.backend.module.patient_queue.service.PatientQueueService;
import com.pulsecare.backend.module.resource.ward.service.WardService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientAdmissionServiceImpl implements PatientAdmissionService {

    private final PatientAdmissionRepository repository;
    private final PatientAdmissionMapper mapper;
    private final PatientService patientService;
    private final PatientQueueService patientQueueService;
    private final WardService wardService;

    public PatientAdmissionServiceImpl(PatientAdmissionRepository repository, @Qualifier("patientAdmissionMapperImpl") PatientAdmissionMapper mapper,
                                       PatientService patientService, PatientQueueService patientQueueService, WardService wardService) {
        this.repository = repository;
        this.mapper = mapper;
        this.patientService = patientService;
        this.patientQueueService = patientQueueService;
        this.wardService = wardService;
    }

    @Override
    public PatientAdmissionResDTO findById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient admission with id " + id + " not found")));
    }

    @Override
    public PatientAdmission findEntityById(Long id) {
        return repository.findById(id)
                        .orElseThrow(() ->  new ResourceNotFoundException("Patient admission with id " + id + " not found"));
    }

    @Override
    public List<PatientAdmissionResDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
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
