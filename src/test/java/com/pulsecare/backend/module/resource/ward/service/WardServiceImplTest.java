package com.pulsecare.backend.module.resource.ward.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.resource.ward.model.Ward;
import com.pulsecare.backend.module.resource.ward.repository.WardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WardServiceImplTest {

    @Mock private WardRepository repository;
    @InjectMocks private WardServiceImpl service;

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1));
        verify(repository).findById(1);
    }

    @Test
    void validateWardNameAndDepartmentIDUniqueness_whenOtherWardExists_throwsAlreadyExists() {
        Ward existing = new Ward();
        existing.setId(99);

        when(repository.findByNameAndDepartmentId("A", 1)).thenReturn(Optional.of(existing));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.validateWardNameAndDepartmentIDUniqueness("A", 1, 1));

        verify(repository).findByNameAndDepartmentId("A", 1);
    }

    @Test
    void validateWardNameAndDepartmentIDUniqueness_whenSameWard_ok() {
        Ward existing = new Ward();
        existing.setId(1);

        when(repository.findByNameAndDepartmentId("A", 1)).thenReturn(Optional.of(existing));

        assertDoesNotThrow(() -> service.validateWardNameAndDepartmentIDUniqueness("A", 1, 1));
        verify(repository).findByNameAndDepartmentId("A", 1);
    }

    @Test
    void findWardByWardIdAndDepartmentId_whenMissing_throwsNotFound() {
        when(repository.findWardByIdAndDepartmentId(1, 10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findWardByWardIdAndDepartmentId(1, 10));

        verify(repository).findWardByIdAndDepartmentId(1, 10);
    }
}