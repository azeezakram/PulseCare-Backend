package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.resource.department.model.Department;
import com.pulsecare.backend.module.resource.department.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock private DepartmentRepository repository;
    @InjectMocks private DepartmentServiceImpl service;

    @Test
    void findById_whenExists_returnsEntity() {
        Department d = new Department();
        d.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(d));

        Department result = service.findById(1);

        assertSame(d, result);
        verify(repository).findById(1);
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
        verify(repository).findById(1);
    }

    @Test
    void findAll_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new Department(), new Department()));

        assertEquals(2, service.findAll().size());
        verify(repository).findAll();
    }

    @Test
    void save_callsRepositorySave() {
        Department d = new Department();
        when(repository.save(d)).thenReturn(d);

        assertSame(d, service.save(d));
        verify(repository).save(d);
    }

    @Test
    void delete_whenExists_deletesEntity() {
        Department d = new Department();
        d.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(d));

        service.delete(1);

        verify(repository).findById(1);
        verify(repository).delete(d);
    }

    @Test
    void validateNameUniqueness_whenOtherDeptHasSameName_throwsAlreadyExists() {
        Department existing = new Department();
        existing.setId(99);

        when(repository.findByName("OPD")).thenReturn(Optional.of(existing));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.validateNameUniqueness("OPD", 1));

        verify(repository).findByName("OPD");
    }

    @Test
    void validateNameUniqueness_whenSameDept_ok() {
        Department existing = new Department();
        existing.setId(1);

        when(repository.findByName("OPD")).thenReturn(Optional.of(existing));

        assertDoesNotThrow(() -> service.validateNameUniqueness("OPD", 1));
        verify(repository).findByName("OPD");
    }

    @Test
    void validateNameDoesNotExist_whenExists_throwsAlreadyExists() {
        when(repository.findByName("OPD")).thenReturn(Optional.of(new Department()));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.validateNameDoesNotExist("OPD"));

        verify(repository).findByName("OPD");
    }

    @Test
    void validateNameDoesNotExist_whenMissing_ok() {
        when(repository.findByName("OPD")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.validateNameDoesNotExist("OPD"));
        verify(repository).findByName("OPD");
    }
}