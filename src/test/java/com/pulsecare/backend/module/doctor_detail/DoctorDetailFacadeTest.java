package com.pulsecare.backend.module.doctor_detail;

import com.pulsecare.backend.module.doctor_detail.dto.DoctorDetailReqDto;
import com.pulsecare.backend.module.doctor_detail.dto.DoctorDetailResDto;
import com.pulsecare.backend.module.doctor_detail.facade.DoctorDetailFacade;
import com.pulsecare.backend.module.doctor_detail.mapper.DoctorDetailMapper;
import com.pulsecare.backend.module.doctor_detail.model.DoctorDetail;
import com.pulsecare.backend.module.doctor_detail.service.DoctorDetailService;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.specialization.service.SpecializationService;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorDetailFacadeTest {

    @Mock private DoctorDetailService doctorDetailService;
    @Mock private UserService userService;
    @Mock private SpecializationService specializationService;
    @Mock private DoctorDetailMapper doctorDetailMapper;

    @InjectMocks private DoctorDetailFacade facade;

    @Test
    void createNewDoctorDetail_withSpecializations_setsUserAndSpecs_saves() {
        UUID userId = UUID.randomUUID();
        Set<Integer> specIds = Set.of(1, 2);

        DoctorDetailReqDto req = new DoctorDetailReqDto("LIC-1", userId.toString(), specIds);

        DoctorDetail entity = new DoctorDetail();
        Users user = new Users();
        user.setId(userId);

        Specialization s1 = new Specialization();
        Specialization s2 = new Specialization();

        when(doctorDetailMapper.toEntity(req)).thenReturn(entity);
        when(userService.findById(userId.toString())).thenReturn(user);
        when(specializationService.findAllById(specIds)).thenReturn(new HashSet<>(Arrays.asList(s1, s2)));

        DoctorDetail saved = new DoctorDetail();
        DoctorDetailResDto dto = mock(DoctorDetailResDto.class);

        when(doctorDetailService.save(entity)).thenReturn(saved);
        when(doctorDetailMapper.toDTO(saved)).thenReturn(dto);

        DoctorDetailResDto result = facade.createNewDoctorDetail(req);

        assertSame(dto, result);

        verify(doctorDetailService).validateAlreadyHasDoctorDetail(userId);
        verify(doctorDetailService).validateLicenseNoUniqueness("LIC-1", userId);
        verify(userService).findById(userId.toString());
        assertSame(user, entity.getUser());
        assertEquals(2, entity.getSpecializations().size());

        verify(doctorDetailService).save(entity);
        verify(doctorDetailMapper).toDTO(saved);
    }

    @Test
    void updateDoctorDetail_whenLicenseChanges_validatesUniqueness_updatesAndSaves() {
        UUID userId = UUID.randomUUID();
        DoctorDetailReqDto req = new DoctorDetailReqDto("NEW-LIC", null, Set.of());

        DoctorDetail existing = new DoctorDetail();
        existing.setLicenseNo("OLD-LIC");

        when(doctorDetailService.findByUserId(userId.toString())).thenReturn(existing);

        DoctorDetail saved = new DoctorDetail();
        DoctorDetailResDto dto = mock(DoctorDetailResDto.class);
        when(doctorDetailService.save(existing)).thenReturn(saved);
        when(doctorDetailMapper.toDTO(saved)).thenReturn(dto);

        DoctorDetailResDto result = facade.updateDoctorDetail(req, userId.toString());

        assertSame(dto, result);

        verify(doctorDetailService).findByUserId(userId.toString());
        verify(doctorDetailService).validateLicenseNoUniqueness("NEW-LIC", userId);
        verify(doctorDetailMapper).updateEntity(req, existing);
        verify(doctorDetailService).save(existing);
    }

    @Test
    void updateDoctorDetail_whenLicenseSame_skipsUniquenessValidation() {
        UUID userId = UUID.randomUUID();
        DoctorDetailReqDto req = new DoctorDetailReqDto("LIC-1", null, null);

        DoctorDetail existing = new DoctorDetail();
        existing.setLicenseNo("LIC-1");

        when(doctorDetailService.findByUserId(userId.toString())).thenReturn(existing);
        when(doctorDetailService.save(existing)).thenReturn(existing);
        when(doctorDetailMapper.toDTO(existing)).thenReturn(mock(DoctorDetailResDto.class));

        facade.updateDoctorDetail(req, userId.toString());

        verify(doctorDetailService, never()).validateLicenseNoUniqueness(anyString(), any());
        verify(doctorDetailMapper).updateEntity(req, existing);
    }
}
