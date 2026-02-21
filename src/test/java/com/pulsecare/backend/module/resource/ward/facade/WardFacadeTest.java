package com.pulsecare.backend.module.resource.ward.facade;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import com.pulsecare.backend.module.resource.ward.dto.WardReqDTO;
import com.pulsecare.backend.module.resource.ward.dto.WardResDTO;
import com.pulsecare.backend.module.resource.ward.maper.WardMapper;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import com.pulsecare.backend.module.resource.ward.service.WardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WardFacadeTest {

    @Mock private WardService wardService;
    @Mock private DepartmentService departmentService;
    @Mock private WardMapper mapper;

    @InjectMocks private WardFacade facade;

    @Test
    void createWard_whenDepartmentIdNull_throwsValidation() {
        WardReqDTO req = new WardReqDTO("A", null, 10, 0);

        assertThrows(ValidationException.class, () -> facade.createWard(req));

        verifyNoInteractions(wardService, departmentService, mapper);
    }

    @Test
    void createWard_success_validates_maps_setsDepartment_saves_mapsBack() {
        WardReqDTO req = new WardReqDTO("A", 1, 10, 0);

        Ward entity = new Ward();
        Department dept = new Department();
        Ward saved = new Ward();
        WardResDTO dto = mock(WardResDTO.class);

        when(mapper.toEntity(req)).thenReturn(entity);
        when(departmentService.findById(1)).thenReturn(dept);
        when(wardService.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(dto);

        WardResDTO result = facade.createWard(req);

        assertSame(dto, result);
        assertSame(dept, entity.getDepartment());

        verify(wardService).validateWardNameAndDepartmentIDUniqueness("A", 1, null);
        verify(mapper).toEntity(req);
        verify(departmentService).findById(1);
        verify(wardService).save(entity);
        verify(mapper).toDTO(saved);
    }

    @Test
    void updateWard_whenDepartmentIdNull_throwsValidation() {
        WardReqDTO req = new WardReqDTO("A", null, 10, 0);

        assertThrows(ValidationException.class, () -> facade.updateWard(req, 1));
        verifyNoInteractions(wardService, departmentService, mapper);
    }

    @Test
    void updateWard_whenNameBlank_throwsValidation() {
        WardReqDTO req = new WardReqDTO("   ", 1, 10, 0);

        assertThrows(ValidationException.class, () -> facade.updateWard(req, 1));
        verifyNoInteractions(wardService, departmentService, mapper);
    }

    @Test
    void updateWard_success_validates_updates_setsDepartment_saves_mapsBack() {
        WardReqDTO req = new WardReqDTO("NEW", 1, 10, 0);

        Ward existing = new Ward();
        Department dept = new Department();
        Ward saved = new Ward();
        WardResDTO dto = mock(WardResDTO.class);

        when(wardService.findWardByWardIdAndDepartmentId(5, 1)).thenReturn(existing);
        when(departmentService.findById(1)).thenReturn(dept);
        when(wardService.save(existing)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(dto);

        WardResDTO result = facade.updateWard(req, 5);

        assertSame(dto, result);
        assertSame(dept, existing.getDepartment());

        verify(wardService).validateWardNameAndDepartmentIDUniqueness("NEW", 1, 5);
        verify(mapper).updateEntity(req, existing);
        verify(departmentService).findById(1);
        verify(wardService).save(existing);
        verify(mapper).toDTO(saved);
    }
}