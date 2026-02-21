package com.pulsecare.backend.module.resource.bed;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.resource.bed.dto.BedReqDTO;
import com.pulsecare.backend.module.resource.bed.dto.BedResDTO;
import com.pulsecare.backend.module.resource.bed.mapper.BedMapper;
import com.pulsecare.backend.module.resource.bed.model.Bed;
import com.pulsecare.backend.module.resource.bed.repository.BedRepository;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import com.pulsecare.backend.module.resource.ward.service.WardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BedServiceImplTest {

    @Mock
    private BedRepository repository;
    @Mock
    private BedMapper mapper;
    @Mock
    private WardService wardService;

    @InjectMocks
    private BedServiceImpl service;

    @Test
    void save_whenBedNoExistsInWard_throwsAlreadyExists() {
        BedReqDTO req = new BedReqDTO("B-1", false, 10);

        when(repository.findByBedNoAndWard_Id("B-1", 10)).thenReturn(Optional.of(new Bed()));

        assertThrows(ResourceAlreadyExistsException.class, () -> service.save(req));

        verify(repository).findByBedNoAndWard_Id("B-1", 10);
        verifyNoInteractions(wardService, mapper);
        verify(repository, never()).save(any());
    }

    @Test
    void save_whenBedNoNull_generatesBedNo_andUpdatesWardCounts() {
        BedReqDTO req = new BedReqDTO(null, false, 10);

        Ward ward = new Ward();
        ward.setId(10);
        ward.setName("WardA");
        ward.setBedCount(2);
        ward.setOccupiedBeds(1);

        Bed bedEntity = new Bed();
        bedEntity.setIsTaken(null);

        Bed firstSave = new Bed();
        firstSave.setId(7L);
        firstSave.setIsTaken(false);

        Bed secondSave = new Bed();
        secondSave.setId(7L);
        secondSave.setIsTaken(false);

        BedResDTO res = mock(BedResDTO.class);

        when(wardService.findById(10)).thenReturn(ward);
        when(mapper.toEntity(req)).thenReturn(bedEntity);

        when(repository.save(any(Bed.class))).thenReturn(firstSave, secondSave);

        when(mapper.toDTO(secondSave)).thenReturn(res);

        BedResDTO result = service.save(req);

        assertSame(res, result);

        assertEquals(3, ward.getBedCount());
        assertEquals(1, ward.getOccupiedBeds());

        verify(wardService).save(ward);

        assertEquals("WA-7", firstSave.getBedNo());
    }

    @Test
    void update_whenToggleTakenFalseToTrue_incrementsOccupied() {
        BedReqDTO req = new BedReqDTO("B-2", true, 10);

        Ward ward = new Ward();
        ward.setId(10);
        ward.setOccupiedBeds(1);

        Bed existing = new Bed();
        existing.setId(5L);
        existing.setIsTaken(false);

        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(wardService.findById(10)).thenReturn(ward);

        doAnswer(inv -> {
            Bed target = inv.getArgument(1);
            target.setIsTaken(true);
            return null;
        }).when(mapper).updateEntity(eq(req), eq(existing));

        when(repository.save(existing)).thenReturn(existing);
        when(mapper.toDTO(existing)).thenReturn(mock(BedResDTO.class));

        service.update(5L, req);

        assertEquals(2, ward.getOccupiedBeds());
        assertSame(ward, existing.getWard());
        verify(repository).save(existing);
    }

    @Test
    void delete_whenTaken_decrementsWardCounts() {
        Ward ward = new Ward();
        ward.setBedCount(5);
        ward.setOccupiedBeds(2);

        Bed bed = new Bed();
        bed.setId(9L);
        bed.setIsTaken(true);
        bed.setWard(ward);

        when(repository.findById(9L)).thenReturn(Optional.of(bed));

        service.delete(9L);

        assertEquals(4, ward.getBedCount());
        assertEquals(1, ward.getOccupiedBeds());

        verify(repository).delete(bed);
        verify(wardService).save(ward);
    }

    @Test
    void findEntityById_whenMissing_throwsNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findEntityById(1L));
        verify(repository).findById(1L);
    }
}