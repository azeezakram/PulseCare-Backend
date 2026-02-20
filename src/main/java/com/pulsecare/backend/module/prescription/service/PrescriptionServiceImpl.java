package com.pulsecare.backend.module.prescription.service;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient_admission.enums.PatientAdmissionStatus;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import com.pulsecare.backend.module.patient_admission.service.PatientAdmissionService;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.patient_queue.service.PatientQueueService;
import com.pulsecare.backend.module.prescription.dto.*;
import com.pulsecare.backend.module.prescription.enums.PrescriptionStatus;
import com.pulsecare.backend.module.prescription.enums.PrescriptionType;
import com.pulsecare.backend.module.prescription.mapper.PrescriptionItemMapper;
import com.pulsecare.backend.module.prescription.mapper.PrescriptionMapper;
import com.pulsecare.backend.module.prescription.model.Prescription;
import com.pulsecare.backend.module.prescription.model.PrescriptionItem;
import com.pulsecare.backend.module.prescription.repository.PrescriptionItemRepository;
import com.pulsecare.backend.module.prescription.repository.PrescriptionRepository;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PatientQueueService patientQueueService;
    private final PatientAdmissionService patientAdmissionService;
    private final UserService userService;
    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionItemMapper prescriptionItemMapper;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, PrescriptionItemRepository prescriptionItemRepository, PatientQueueService patientQueueService, PatientAdmissionService patientAdmissionService, UserService userService,
                                   @Qualifier("prescriptionMapperImpl") PrescriptionMapper prescriptionMapper, @Qualifier("prescriptionItemMapperImpl") PrescriptionItemMapper prescriptionItemMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionItemRepository = prescriptionItemRepository;
        this.patientQueueService = patientQueueService;
        this.patientAdmissionService = patientAdmissionService;
        this.userService = userService;
        this.prescriptionMapper = prescriptionMapper;
        this.prescriptionItemMapper = prescriptionItemMapper;
    }

    @Override
    public PrescriptionSummaryResDTO findById(Long id) {
        return prescriptionMapper.toSummaryDTO(
                prescriptionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Patient admission not found"))
        );
    }

    @Override
    public List<PrescriptionDetailResDTO> findAllByAdmissionsId(Long admissionId) {

        List<Prescription> prescriptions =
                prescriptionRepository.findAllByAdmissionId(admissionId);

        return prescriptions.stream()
                .map(p -> prescriptionMapper.toDetailDTO(p, mapItemsToDto(p.getId())))
                .toList();
    }

    private List<PrescriptionItemResDTO> mapItemsToDto(Long prescriptionId) {
        if (prescriptionId == null) return List.of();

        List<PrescriptionItem> items =
                prescriptionItemRepository.findAllByPrescriptionId(prescriptionId);

        return items.stream()
                .map(prescriptionItemMapper::toDTO)
                .toList();
    }

    @Override
    public PrescriptionDetailResDTO findWithDetailById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient admission not found"));

        List<PrescriptionItemResDTO> items = prescriptionItemRepository.findAllByPrescriptionId(prescription.getId()).stream()
                .map(prescriptionMapper::toDTO)
                .toList();

        return prescriptionMapper.toDetailDTO(prescription, items);
    }

    @Override
    public Prescription findEntityById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient admission not found"));
    }

    @Override
    public List<PrescriptionSummaryResDTO> findAll() {
        return prescriptionRepository.findAll().stream()
                .map(prescriptionMapper::toSummaryDTO)
                .toList();
    }

    @Override
    @Transactional
    public PrescriptionDetailResDTO save(PrescriptionReqDTO data) {

        PatientQueue queue = null;
        PatientAdmission admission = null;
        PrescriptionType type;

        if (data.queueId() != null) {
            queue = patientQueueService.findEntityById(data.queueId());

            if (queue.getStatus() != QueueStatus.WAITING) {
                throw new IllegalStateException("Patient is not in WAITING queue status");
            }

            type = PrescriptionType.valueOf(data.type().name());

        } else if (data.admissionId() != null) {
            admission = patientAdmissionService.findEntityById(data.admissionId());

            if (admission.getStatus() != PatientAdmissionStatus.ACTIVE) {
                throw new IllegalStateException("Patient is not currently admitted");
            }

            type = PrescriptionType.IPD;

        } else {
            throw new IllegalArgumentException("Either queueId or admissionId must be provided");
        }

        Users doctor = userService.findById(data.doctorId());

        boolean hasItems = data.items() != null && !data.items().isEmpty();
        PrescriptionStatus status = hasItems
                ? PrescriptionStatus.FINALIZED
                : PrescriptionStatus.DRAFT;

        Prescription prescription = Prescription.builder()
                .doctor(doctor)
                .patientQueue(queue)
                .admission(admission)
                .type(type)
                .notes(data.notes())
                .status(status)
                .build();

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        if (hasItems) {
            List<PrescriptionItem> items = data.items().stream()
                    .map(itemDto -> {
                        PrescriptionItem item = prescriptionItemMapper.toEntity(itemDto);
                        item.setPrescription(savedPrescription);
                        return item;
                    })
                    .toList();

            prescriptionItemRepository.saveAll(items);
        }

        List<PrescriptionItemResDTO> resItems =
                hasItems
                        ? prescriptionItemRepository.findAllByPrescriptionId(savedPrescription.getId())
                        .stream()
                        .map(prescriptionMapper::toDTO)
                        .toList()
                        : List.of();

        return prescriptionMapper.toDetailDTO(savedPrescription, resItems);
    }

    @Override
    @Transactional
    public PrescriptionDetailResDTO update(Long id, PrescriptionReqDTO data) {

        Prescription prescription = findEntityById(id);

        if (prescription.getStatus() == PrescriptionStatus.DISPENSED) {
            throw new IllegalStateException("Cannot update dispensed prescription");
        }

        if (data.notes() != null) {
            prescription.setNotes(data.notes());
        }

        updateItems(prescription, data);

        Prescription saved = prescriptionRepository.save(prescription);

        List<PrescriptionItemResDTO> resItems =
                prescriptionItemRepository.findAllByPrescriptionId(saved.getId())
                        .stream()
                        .map(prescriptionMapper::toDTO)
                        .toList();

        return prescriptionMapper.toDetailDTO(saved, resItems);
    }

    private void updateItems(Prescription prescription, PrescriptionReqDTO data) {
        if (data.items() == null) return;

        List<PrescriptionItem> existingItems =
                prescriptionItemRepository.findAllByPrescriptionId(prescription.getId());

        Map<Long, PrescriptionItem> existingMap = existingItems.stream()
                .collect(Collectors.toMap(PrescriptionItem::getId, Function.identity()));

        Set<Long> incomingIds = new HashSet<>();

        for (PrescriptionItemReqDTO itemDto : data.items()) {
            if (itemDto.id() != null && existingMap.containsKey(itemDto.id())) {
                prescriptionItemMapper.updateEntity(itemDto, existingMap.get(itemDto.id()));
                incomingIds.add(itemDto.id());
            } else {
                createNewItem(prescription, itemDto, incomingIds);
            }
        }

        deleteRemovedItems(existingItems, incomingIds);

        prescription.setStatus(data.items().isEmpty() ? PrescriptionStatus.DRAFT : PrescriptionStatus.FINALIZED);
    }

    private void createNewItem(Prescription prescription, PrescriptionItemReqDTO dto, Set<Long> incomingIds) {
        PrescriptionItem newItem = PrescriptionItem.builder()
                .prescription(prescription)
                .medicineName(dto.medicineName())
                .dosage(dto.dosage())
                .frequency(dto.frequency())
                .durationDays(dto.durationDays())
                .instructions(dto.instructions())
                .build();
        prescriptionItemRepository.save(newItem);
        incomingIds.add(newItem.getId());
    }

    private void deleteRemovedItems(List<PrescriptionItem> existingItems, Set<Long> incomingIds) {
        for (PrescriptionItem oldItem : existingItems) {
            if (!incomingIds.contains(oldItem.getId())) {
                prescriptionItemRepository.delete(oldItem);
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Prescription entity = findEntityById(id);
        prescriptionRepository.delete(entity);
    }

}
