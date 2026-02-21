package com.pulsecare.backend.module.resource.department.facade;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.resource.department.mapper.DepartmentMapper;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentFacadeTest {

    @Mock private DepartmentService departmentService;
    @Mock private DepartmentMapper departmentMapper;
    @InjectMocks private DepartmentFacade facade;

    @Test
    void createDepartment_validates_maps_saves_mapsBack() {
        DeptRequestDTO req = new DeptRequestDTO("OPD");

        Department entity = new Department();
        Department saved = new Department();
        DeptResponseDTO dto = mock(DeptResponseDTO.class);

        when(departmentMapper.toEntity(req)).thenReturn(entity);
        when(departmentService.save(entity)).thenReturn(saved);
        when(departmentMapper.toDTO(saved)).thenReturn(dto);

        DeptResponseDTO result = facade.createDepartment(req);

        assertSame(dto, result);
        verify(departmentService).validateNameDoesNotExist("OPD");
        verify(departmentMapper).toEntity(req);
        verify(departmentService).save(entity);
        verify(departmentMapper).toDTO(saved);
    }

    @Test
    void updateDepartment_whenNameBlank_throwsValidation() {
        DeptRequestDTO req = new DeptRequestDTO("   ");

        Department existing = new Department();
        when(departmentService.findById(1)).thenReturn(existing);

        assertThrows(ValidationException.class, () -> facade.updateDepartment(req, 1));

        verify(departmentService).findById(1);
        verify(departmentService, never()).save(any());
        verifyNoInteractions(departmentMapper);
    }

    @Test
    void updateDepartment_whenNameProvided_validates_uniqueness_setsName_saves_mapsBack() {
        DeptRequestDTO req = new DeptRequestDTO("NEW");

        Department existing = new Department();
        existing.setId(1);
        existing.setName("OLD");

        Department saved = new Department();
        DeptResponseDTO dto = mock(DeptResponseDTO.class);

        when(departmentService.findById(1)).thenReturn(existing);
        when(departmentService.save(existing)).thenReturn(saved);
        when(departmentMapper.toDTO(saved)).thenReturn(dto);

        DeptResponseDTO result = facade.updateDepartment(req, 1);

        assertSame(dto, result);
        assertEquals("NEW", existing.getName());

        verify(departmentService).findById(1);
        verify(departmentService).validateNameUniqueness("NEW", 1);
        verify(departmentService).save(existing);
        verify(departmentMapper).toDTO(saved);
    }

    @Test
    void updateDepartment_whenNameNull_onlySavesExisting() {
        DeptRequestDTO req = new DeptRequestDTO(null);

        Department existing = new Department();
        when(departmentService.findById(1)).thenReturn(existing);

        Department saved = new Department();
        DeptResponseDTO dto = mock(DeptResponseDTO.class);
        when(departmentService.save(existing)).thenReturn(saved);
        when(departmentMapper.toDTO(saved)).thenReturn(dto);

        DeptResponseDTO result = facade.updateDepartment(req, 1);

        assertSame(dto, result);

        verify(departmentService).findById(1);
        verify(departmentService, never()).validateNameUniqueness(anyString(), anyInt());
        verify(departmentService).save(existing);
        verify(departmentMapper).toDTO(saved);
    }
}