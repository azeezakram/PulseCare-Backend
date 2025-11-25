package com.pulsecare.backend.module.doctordetail.facade;

import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailReqDto;
import com.pulsecare.backend.module.doctordetail.dto.DoctorDetailResDto;
import com.pulsecare.backend.module.doctordetail.mapper.DoctorDetailMapper;
import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;
import com.pulsecare.backend.module.doctordetail.service.DoctorDetailService;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.specialization.service.SpecializationService;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DoctorDetailFacade {

    private final DoctorDetailService doctorDetailService;
    private final UserService userService;
    private final SpecializationService specializationService;
    private final DoctorDetailMapper doctorDetailMapper;

    public DoctorDetailFacade(DoctorDetailService doctorDetailService, UserService userService, SpecializationService specializationService, DoctorDetailMapper doctorDetailMapper) {
        this.doctorDetailService = doctorDetailService;
        this.userService = userService;
        this.specializationService = specializationService;
        this.doctorDetailMapper = doctorDetailMapper;
    }

    public DoctorDetailResDto create(DoctorDetailReqDto responseDto) {
        Users user = userService.findById(responseDto.userId());
        Set<Specialization> specializations = specializationService.findAllById(responseDto.specializationIds());

        DoctorDetail doctorDetail = new DoctorDetail();
        doctorDetail.setUser(user);
        doctorDetail.setLicenseNo(responseDto.licenseNo());
        doctorDetail.setSpecializations(specializations);

        return doctorDetailMapper.toDTO(doctorDetailService.create(doctorDetail));
    }

    public DoctorDetailResDto update(DoctorDetailReqDto requestDto, String id) {
        DoctorDetail existing = doctorDetailService.findByUserId(id);
        Users user = userService.findById(requestDto.userId());
        Set<Specialization> specializations = new HashSet<>(
                specializationService.findAllById(
                        requestDto.specializationIds()
                )
        );

        existing.setUser(user);
        existing.setLicenseNo(requestDto.licenseNo());
        existing.setSpecializations(specializations);

        return doctorDetailMapper.toDTO(doctorDetailService.update(id, existing));
    }
}
