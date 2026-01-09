package com.pulsecare.backend.module.user.controller;

import com.pulsecare.backend.common.base.controller.CreatableController;
import com.pulsecare.backend.common.base.controller.DeletableController;
import com.pulsecare.backend.common.base.controller.FindableController;
import com.pulsecare.backend.common.base.controller.UpdatableController;
import com.pulsecare.backend.common.template.response.ResponseBody;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserController extends
        FindableController<ResponseBody<UserResponseDTO>, ResponseBody<List<UserResponseDTO>>, String>,
        CreatableController<UserRequestDTO, ResponseBody<UserResponseDTO>>,
        UpdatableController<UserRequestDTO, ResponseBody<UserResponseDTO>, String>,
        DeletableController<ResponseBody<String>, String> {
    ResponseEntity<ResponseBody<UserResponseDTO>> findByUsername(@PathVariable String username);
    ResponseEntity<byte[]> fetchProfileImage(@PathVariable UUID id);
    ResponseEntity<String> saveProfileImage(@PathVariable UUID id, @RequestPart("image") MultipartFile image);
}
