package com.pulsecare.backend.module.doctor_detail;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.doctor_detail.model.DoctorDetail;
import com.pulsecare.backend.module.doctor_detail.repository.DoctorDetailRepository;
import com.pulsecare.backend.module.doctor_detail.service.DoctorDetailServiceImpl;
import com.pulsecare.backend.module.user.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorDetailServiceImplTest {

    @Mock private DoctorDetailRepository repository;
    @InjectMocks private DoctorDetailServiceImpl service;

    private UUID userId;
    private DoctorDetail detail;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        detail = new DoctorDetail();
        Users u = new Users();
        u.setId(userId);
        detail.setUser(u);
        detail.setLicenseNo("LIC-1");
    }

    @Test
    void findByUserId_whenMissing_throwsNotFound() {
        when(repository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByUserId(userId.toString()));
        verify(repository).findByUserId(userId);
    }

    @Test
    void validateAlreadyHasDoctorDetail_whenExists_throwsAlreadyExists() {
        when(repository.findByUserId(userId)).thenReturn(Optional.of(detail));

        assertThrows(ResourceAlreadyExistsException.class, () -> service.validateAlreadyHasDoctorDetail(userId));
        verify(repository).findByUserId(userId);
    }

    @Test
    void validateLicenseNoUniqueness_whenTakenByOtherUser_throwsAlreadyExists() {
        UUID otherId = UUID.randomUUID();
        Users other = new Users();
        other.setId(otherId);

        DoctorDetail existing = new DoctorDetail();
        existing.setUser(other);
        existing.setLicenseNo("LIC-1");

        when(repository.findByLicenseNo("LIC-1")).thenReturn(Optional.of(existing));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.validateLicenseNoUniqueness("LIC-1", userId));

        verify(repository).findByLicenseNo("LIC-1");
    }

    @Test
    void validateLicenseNoUniqueness_whenSameUser_ok() {
        when(repository.findByLicenseNo("LIC-1")).thenReturn(Optional.of(detail));

        assertDoesNotThrow(() -> service.validateLicenseNoUniqueness("LIC-1", userId));
        verify(repository).findByLicenseNo("LIC-1");
    }
}
