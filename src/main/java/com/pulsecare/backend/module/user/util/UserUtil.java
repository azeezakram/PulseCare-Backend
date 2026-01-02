package com.pulsecare.backend.module.user.util;

import com.pulsecare.backend.module.user.model.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserUtil {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static void addDataToEntity(Users data, Users existingById) {
        if (data.getFirstName() != null) {
            existingById.setFirstName(data.getFirstName());
        }
        if (data.getLastName() != null) {
            existingById.setLastName(data.getLastName());
        }
        if (data.getUsername() != null) {
            existingById.setUsername(data.getUsername());
        }
        if (data.getPassword() != null) {
            existingById.setPassword(PASSWORD_ENCODER.encode(data.getPassword()));
        }
        if (data.getEmail() != null) {
            existingById.setEmail(data.getEmail());
        }
        if (data.getMobileNumber() != null) {
            existingById.setMobileNumber(data.getMobileNumber());
        }
        if (data.getImageName() != null) {
            existingById.setImageName(data.getImageName());
        }
        if (data.getContentType() != null) {
            existingById.setContentType(data.getContentType());
        }
        if (data.getImageData() != null) {
            existingById.setImageData(data.getImageData());
        }
        if (data.getIsActive() != null) {
            existingById.setIsActive(data.getIsActive());
        }
        if (data.getRole() != null) {
            existingById.setRole(data.getRole());
        }
        if (data.getDoctorDetails() != null) {
            existingById.setDoctorDetails(data.getDoctorDetails());
        }
    }
}
