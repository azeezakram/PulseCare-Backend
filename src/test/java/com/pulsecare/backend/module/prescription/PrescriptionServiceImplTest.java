package com.pulsecare.backend.module.prescription;

import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.patient_admission.enums.PatientAdmissionStatus;
import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import com.pulsecare.backend.module.patient_admission.service.PatientAdmissionService;
import com.pulsecare.backend.module.patient_queue.enums.QueueStatus;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.patient_queue.service.PatientQueueService;
import com.pulsecare.backend.module.prescription.dto.PrescriptionDetailResDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionItemReqDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionItemResDTO;
import com.pulsecare.backend.module.prescription.dto.PrescriptionReqDTO;
import com.pulsecare.backend.module.prescription.enums.PrescriptionStatus;
import com.pulsecare.backend.module.prescription.enums.PrescriptionType;
import com.pulsecare.backend.module.prescription.mapper.PrescriptionItemMapper;
import com.pulsecare.backend.module.prescription.mapper.PrescriptionMapper;
import com.pulsecare.backend.module.prescription.model.Prescription;
import com.pulsecare.backend.module.prescription.model.PrescriptionItem;
import com.pulsecare.backend.module.prescription.repository.PrescriptionItemRepository;
import com.pulsecare.backend.module.prescription.repository.PrescriptionRepository;
import com.pulsecare.backend.module.prescription.service.PrescriptionServiceImpl;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;
    @Mock
    private PrescriptionItemRepository prescriptionItemRepository;
    @Mock
    private PatientQueueService patientQueueService;
    @Mock
    private PatientAdmissionService patientAdmissionService;
    @Mock
    private UserService userService;
    @Mock
    private PrescriptionMapper prescriptionMapper;
    @Mock
    private PrescriptionItemMapper prescriptionItemMapper;

    @InjectMocks
    private PrescriptionServiceImpl service;

    private PrescriptionReqDTO reqWithQueue(List<PrescriptionItemReqDTO> items) {
        return new PrescriptionReqDTO(
                "doctor-uuid",
                10L,
                null,
                PrescriptionType.OPD,
                "notes",
                items
        );
    }

    private PrescriptionReqDTO reqWithAdmission(List<PrescriptionItemReqDTO> items) {
        return new PrescriptionReqDTO(
                "doctor-uuid",
                null,
                99L,
                PrescriptionType.OPD,
                "notes",
                items
        );
    }

    private PrescriptionItemReqDTO itemReq(Long id, String name) {
        return new PrescriptionItemReqDTO(
                id,
                null,
                name,
                "1 tab",
                "BD",
                5,
                "after food"
        );
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
        verify(prescriptionRepository).findById(1L);
        verifyNoInteractions(prescriptionMapper);
    }

    @Test
    void findEntityById_whenMissing_throwsNotFound() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findEntityById(1L));
        verify(prescriptionRepository).findById(1L);
    }

    @Test
    void save_whenQueueIdProvided_andQueueNotWaiting_throwsIllegalState() {
        PatientQueue queue = new PatientQueue();
        queue.setStatus(QueueStatus.ADMITTED);

        when(patientQueueService.findEntityById(10L)).thenReturn(queue);

        PrescriptionReqDTO req = reqWithQueue(List.of());

        assertThrows(IllegalStateException.class, () -> service.save(req));

        verify(patientQueueService).findEntityById(10L);
        verifyNoInteractions(userService, prescriptionRepository, prescriptionItemRepository, prescriptionItemMapper, prescriptionMapper, patientAdmissionService);
    }

    @Test
    void save_whenAdmissionIdProvided_andAdmissionNotActive_throwsIllegalState() {
        PatientAdmission admission = new PatientAdmission();
        admission.setStatus(PatientAdmissionStatus.DISCHARGED);

        when(patientAdmissionService.findEntityById(99L)).thenReturn(admission);

        PrescriptionReqDTO req = reqWithAdmission(List.of());

        assertThrows(IllegalStateException.class, () -> service.save(req));

        verify(patientAdmissionService).findEntityById(99L);
        verifyNoInteractions(userService, prescriptionRepository, prescriptionItemRepository, prescriptionItemMapper, prescriptionMapper, patientQueueService);
    }

    @Test
    void save_whenNeitherQueueNorAdmissionProvided_throwsIllegalArgument() {
        PrescriptionReqDTO req = new PrescriptionReqDTO(
                "doctor-uuid",
                null,
                null,
                PrescriptionType.OPD,
                "notes",
                List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> service.save(req));
        verifyNoInteractions(prescriptionRepository, prescriptionItemRepository, patientQueueService, patientAdmissionService, userService);
    }

    @Test
    void save_queuePath_withItems_setsFinalized_savesItems_returnsDetail() {
        // queue WAITING
        PatientQueue queue = new PatientQueue();
        queue.setId(10L);
        queue.setStatus(QueueStatus.WAITING);
        when(patientQueueService.findEntityById(10L)).thenReturn(queue);

        // doctor
        Users doctor = new Users();
        when(userService.findById("doctor-uuid")).thenReturn(doctor);

        List<PrescriptionItemReqDTO> itemsReq = List.of(
                itemReq(null, "Panadol"),
                itemReq(null, "Amox")
        );
        PrescriptionReqDTO req = reqWithQueue(itemsReq);

        Prescription savedPrescription = Prescription.builder().id(7L).build();
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(savedPrescription);

        PrescriptionItem item1 = new PrescriptionItem();
        PrescriptionItem item2 = new PrescriptionItem();
        when(prescriptionItemMapper.toEntity(itemsReq.get(0))).thenReturn(item1);
        when(prescriptionItemMapper.toEntity(itemsReq.get(1))).thenReturn(item2);

        PrescriptionItemResDTO res1 = mock(PrescriptionItemResDTO.class);
        PrescriptionItemResDTO res2 = mock(PrescriptionItemResDTO.class);
        when(prescriptionItemRepository.findAllByPrescriptionId(7L)).thenReturn(List.of(new PrescriptionItem(), new PrescriptionItem()));
        when(prescriptionMapper.toDTO(any(PrescriptionItem.class))).thenReturn(res1, res2);

        PrescriptionDetailResDTO out = mock(PrescriptionDetailResDTO.class);
        when(prescriptionMapper.toDetailDTO(eq(savedPrescription), anyList())).thenReturn(out);

        PrescriptionDetailResDTO result = service.save(req);

        assertSame(out, result);

        ArgumentCaptor<Prescription> presCaptor = ArgumentCaptor.forClass(Prescription.class);
        verify(prescriptionRepository).save(presCaptor.capture());
        Prescription presArg = presCaptor.getValue();

        assertSame(doctor, presArg.getDoctor());
        assertSame(queue, presArg.getPatientQueue());
        assertNull(presArg.getAdmission());
        assertEquals(PrescriptionType.OPD, presArg.getType());
        assertEquals(PrescriptionStatus.FINALIZED, presArg.getStatus());
        assertEquals("notes", presArg.getNotes());

        // items saved and linked to prescription
        ArgumentCaptor<List<PrescriptionItem>> listCaptor = ArgumentCaptor.forClass((Class) List.class);
        verify(prescriptionItemRepository).saveAll(listCaptor.capture());
        List<PrescriptionItem> savedItemsArgs = listCaptor.getValue();
        assertEquals(2, savedItemsArgs.size());
        assertSame(savedPrescription, savedItemsArgs.get(0).getPrescription());
        assertSame(savedPrescription, savedItemsArgs.get(1).getPrescription());

        verify(prescriptionItemRepository).findAllByPrescriptionId(7L);
        verify(prescriptionMapper).toDetailDTO(eq(savedPrescription), anyList());
    }

    @Test
    void save_queuePath_withoutItems_setsDraft_doesNotSaveItems_returnsDetailWithEmptyItems() {
        PatientQueue queue = new PatientQueue();
        queue.setStatus(QueueStatus.WAITING);
        when(patientQueueService.findEntityById(10L)).thenReturn(queue);

        Users doctor = new Users();
        when(userService.findById("doctor-uuid")).thenReturn(doctor);

        PrescriptionReqDTO req = reqWithQueue(List.of());

        Prescription savedPrescription = Prescription.builder().id(7L).build();
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(savedPrescription);

        PrescriptionDetailResDTO out = mock(PrescriptionDetailResDTO.class);
        when(prescriptionMapper.toDetailDTO(eq(savedPrescription), eq(List.of()))).thenReturn(out);

        PrescriptionDetailResDTO result = service.save(req);

        assertSame(out, result);

        ArgumentCaptor<Prescription> presCaptor = ArgumentCaptor.forClass(Prescription.class);
        verify(prescriptionRepository).save(presCaptor.capture());
        assertEquals(PrescriptionStatus.DRAFT, presCaptor.getValue().getStatus());

        verifyNoInteractions(prescriptionItemMapper);
        verify(prescriptionItemRepository, never()).saveAll(anyList());
        verify(prescriptionItemRepository, never()).findAllByPrescriptionId(anyLong());
    }

    @Test
    void save_admissionPath_activeAdmission_forcesTypeIPD() {
        PatientAdmission admission = new PatientAdmission();
        admission.setId(99L);
        admission.setStatus(PatientAdmissionStatus.ACTIVE);
        when(patientAdmissionService.findEntityById(99L)).thenReturn(admission);

        Users doctor = new Users();
        when(userService.findById("doctor-uuid")).thenReturn(doctor);

        PrescriptionReqDTO req = reqWithAdmission(List.of());

        Prescription savedPrescription = Prescription.builder().id(7L).build();
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(savedPrescription);

        PrescriptionDetailResDTO out = mock(PrescriptionDetailResDTO.class);
        when(prescriptionMapper.toDetailDTO(eq(savedPrescription), eq(List.of()))).thenReturn(out);

        PrescriptionDetailResDTO result = service.save(req);

        assertSame(out, result);

        ArgumentCaptor<Prescription> presCaptor = ArgumentCaptor.forClass(Prescription.class);
        verify(prescriptionRepository).save(presCaptor.capture());
        Prescription presArg = presCaptor.getValue();

        assertSame(admission, presArg.getAdmission());
        assertNull(presArg.getPatientQueue());
        assertEquals(PrescriptionType.IPD, presArg.getType());
        assertEquals(PrescriptionStatus.DRAFT, presArg.getStatus());
    }

    @Test
    void update_whenDispensed_throwsIllegalState() {
        Prescription p = new Prescription();
        p.setId(1L);
        p.setStatus(PrescriptionStatus.DISPENSED);

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(IllegalStateException.class, () -> service.update(1L, reqWithQueue(List.of())));

        verify(prescriptionRepository).findById(1L);
        verify(prescriptionRepository, never()).save(any());
        verifyNoInteractions(prescriptionItemRepository);
    }

    @Test
    void update_updatesNotes_andReconcilesItems_statusFinalized() {
        // existing prescription
        Prescription p = new Prescription();
        p.setId(1L);
        p.setStatus(PrescriptionStatus.DRAFT);
        p.setNotes("old");

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(p));

        // existing items in DB: ids 10 and 20
        PrescriptionItem old1 = new PrescriptionItem();
        old1.setId(10L);
        old1.setPrescription(p);

        PrescriptionItem old2 = new PrescriptionItem();
        old2.setId(20L);
        old2.setPrescription(p);

        when(prescriptionItemRepository.findAllByPrescriptionId(1L)).thenReturn(List.of(old1, old2));

        // incoming: update item 10, add new (null id). item 20 should be deleted.
        PrescriptionItemReqDTO upd10 = itemReq(10L, "UpdatedMed");
        PrescriptionItemReqDTO addNew = itemReq(null, "NewMed");
        PrescriptionReqDTO req = new PrescriptionReqDTO(
                "doctor-uuid",
                null,
                null,
                PrescriptionType.OPD,
                "new notes",
                List.of(upd10, addNew)
        );

        doAnswer(inv -> {
            PrescriptionItem target = inv.getArgument(1);
            target.setMedicineName("UpdatedMed");
            return null;
        }).when(prescriptionItemMapper).updateEntity(eq(upd10), eq(old1));

        when(prescriptionItemRepository.save(any(PrescriptionItem.class))).thenAnswer(inv -> {
            PrescriptionItem ni = inv.getArgument(0);
            ni.setId(99L);
            return ni;
        });

        when(prescriptionRepository.save(p)).thenReturn(p);

        PrescriptionItemResDTO dto1 = mock(PrescriptionItemResDTO.class);
        PrescriptionItemResDTO dto2 = mock(PrescriptionItemResDTO.class);
        when(prescriptionItemRepository.findAllByPrescriptionId(1L)).thenReturn(List.of(old1, old2, new PrescriptionItem()));
        when(prescriptionMapper.toDTO(any(PrescriptionItem.class))).thenReturn(dto1, dto2, dto2);

        PrescriptionDetailResDTO out = mock(PrescriptionDetailResDTO.class);
        when(prescriptionMapper.toDetailDTO(eq(p), anyList())).thenReturn(out);

        PrescriptionDetailResDTO result = service.update(1L, req);

        assertSame(out, result);

        assertEquals("new notes", p.getNotes());

        assertEquals(PrescriptionStatus.FINALIZED, p.getStatus());

        verify(prescriptionItemRepository).delete(old2);

        verify(prescriptionRepository).save(p);
        verify(prescriptionMapper).toDetailDTO(eq(p), anyList());
    }

    @Test
    void update_whenItemsEmpty_setsDraft_andDeletesAllExisting() {
        Prescription p = new Prescription();
        p.setId(1L);
        p.setStatus(PrescriptionStatus.FINALIZED);

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(p));

        PrescriptionItem old1 = new PrescriptionItem();
        old1.setId(10L);
        old1.setPrescription(p);

        PrescriptionItem old2 = new PrescriptionItem();
        old2.setId(20L);
        old2.setPrescription(p);

        when(prescriptionItemRepository.findAllByPrescriptionId(1L))
                .thenReturn(List.of(old1, old2))
                .thenReturn(List.of());

        PrescriptionReqDTO req = new PrescriptionReqDTO(
                "doctor-uuid", null, null, PrescriptionType.OPD, null, List.of()
        );

        when(prescriptionRepository.save(p)).thenReturn(p);

        PrescriptionDetailResDTO out = mock(PrescriptionDetailResDTO.class);
        when(prescriptionMapper.toDetailDTO(eq(p), eq(List.of()))).thenReturn(out);

        PrescriptionDetailResDTO result = service.update(1L, req);

        assertSame(out, result);
        assertEquals(PrescriptionStatus.DRAFT, p.getStatus());

        verify(prescriptionItemRepository).delete(old1);
        verify(prescriptionItemRepository).delete(old2);
        verify(prescriptionRepository).save(p);
    }

    @Test
    void findAllByAdmissionsId_mapsEachPrescriptionWithItsItems() {
        Prescription p1 = new Prescription();
        p1.setId(1L);
        Prescription p2 = new Prescription();
        p2.setId(2L);

        when(prescriptionRepository.findAllByAdmissionId(99L)).thenReturn(List.of(p1, p2));

        when(prescriptionItemRepository.findAllByPrescriptionId(1L)).thenReturn(List.of(new PrescriptionItem()));
        when(prescriptionItemRepository.findAllByPrescriptionId(2L)).thenReturn(List.of(new PrescriptionItem(), new PrescriptionItem()));

        PrescriptionItemResDTO itemDto = mock(PrescriptionItemResDTO.class);
        when(prescriptionItemMapper.toDTO(any(PrescriptionItem.class))).thenReturn(itemDto);

        PrescriptionDetailResDTO d1 = mock(PrescriptionDetailResDTO.class);
        PrescriptionDetailResDTO d2 = mock(PrescriptionDetailResDTO.class);

        when(prescriptionMapper.toDetailDTO(eq(p1), anyList())).thenReturn(d1);
        when(prescriptionMapper.toDetailDTO(eq(p2), anyList())).thenReturn(d2);

        List<PrescriptionDetailResDTO> result = service.findAllByAdmissionsId(99L);

        assertEquals(2, result.size());
        assertSame(d1, result.get(0));
        assertSame(d2, result.get(1));

        verify(prescriptionRepository).findAllByAdmissionId(99L);
        verify(prescriptionItemRepository).findAllByPrescriptionId(1L);
        verify(prescriptionItemRepository).findAllByPrescriptionId(2L);
        verify(prescriptionMapper).toDetailDTO(eq(p1), anyList());
        verify(prescriptionMapper).toDetailDTO(eq(p2), anyList());
    }
}