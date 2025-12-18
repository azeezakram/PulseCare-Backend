package com.pulsecare.backend.module.doctor_detail.facade;

import com.pulsecare.backend.module.doctor_detail.dto.DoctorDetailReqDto;
import com.pulsecare.backend.module.doctor_detail.dto.DoctorDetailResDto;
import com.pulsecare.backend.module.doctor_detail.mapper.DoctorDetailMapper;
import com.pulsecare.backend.module.doctor_detail.model.DoctorDetail;
import com.pulsecare.backend.module.doctor_detail.service.DoctorDetailService;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.specialization.service.SpecializationService;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class DoctorDetailFacade {

    private final DoctorDetailService doctorDetailService;
    private final UserService userService;
    private final SpecializationService specializationService;
    private final DoctorDetailMapper doctorDetailMapper;

    public DoctorDetailFacade(DoctorDetailService doctorDetailService, UserService userService,
                              SpecializationService specializationService, @Qualifier("doctorDetailMapperImpl") DoctorDetailMapper doctorDetailMapper) {
        this.doctorDetailService = doctorDetailService;
        this.userService = userService;
        this.specializationService = specializationService;
        this.doctorDetailMapper = doctorDetailMapper;
    }

    @Transactional
    public DoctorDetailResDto createNewDoctorDetail(DoctorDetailReqDto reqDto) {
        doctorDetailService.validateAlreadyHasDoctorDetail(UUID.fromString(reqDto.userId()));
        doctorDetailService.validateLicenseNoUniqueness(reqDto.licenseNo(), UUID.fromString(reqDto.userId()));

        DoctorDetail doctorDetailEntity = doctorDetailMapper.toEntity(reqDto);

        Users user = userService.findById(reqDto.userId());
        doctorDetailEntity.setUser(user);

        if (reqDto.specializationIds() != null) {
            Set<Specialization> specializations = new HashSet<>(
                    specializationService.findAllById(reqDto.specializationIds())
            );
            doctorDetailEntity.setSpecializations(specializations);
        }

        DoctorDetail savedDoctorDetail = doctorDetailService.save(doctorDetailEntity);
        return doctorDetailMapper.toDTO(savedDoctorDetail);

    }

    @Transactional
    public DoctorDetailResDto updateDoctorDetail(DoctorDetailReqDto reqDto, String userId) {
        DoctorDetail existingDetail = doctorDetailService.findByUserId(userId);

        if (reqDto.licenseNo() != null && !reqDto.licenseNo().equals(existingDetail.getLicenseNo())) {
            doctorDetailService.validateLicenseNoUniqueness(reqDto.licenseNo(), UUID.fromString(userId));
        }

        doctorDetailMapper.updateEntity(reqDto, existingDetail);

        if (reqDto.specializationIds() != null && !reqDto.specializationIds().isEmpty()) {
            Set<Specialization> specializations = new HashSet<>(
                    specializationService.findAllById(reqDto.specializationIds())
            );
            existingDetail.setSpecializations(specializations);
        }

        DoctorDetail updatedDetail = doctorDetailService.save(existingDetail);

        return doctorDetailMapper.toDTO(updatedDetail);
    }

}
