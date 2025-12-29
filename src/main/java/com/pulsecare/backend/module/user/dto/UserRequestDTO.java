package com.pulsecare.backend.module.user.dto;

import java.util.Set;

public record UserRequestDTO(

        String firstName,

        String lastName,

        String username,

        String email,

        String password,

        String mobileNumber,

        Set<Integer> roles,

        Boolean isActive
//
//        DoctorDetailReqDto doctorDetails
) {
}
