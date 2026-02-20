package com.pulsecare.backend.module.user.dto;

public interface UserImageProjection {
    byte[] getImageData();
    String getContentType();
}

