package com.pulsecare.backend.module.user.dto;

public record UserRequestDTO(

        String firstName,

        String lastName,

        String username,

        String email,

        String password,

        String mobileNumber,

        Integer roleId,

        Boolean isActive
//
//        DoctorDetailReqDto doctorDetails
) {
}
