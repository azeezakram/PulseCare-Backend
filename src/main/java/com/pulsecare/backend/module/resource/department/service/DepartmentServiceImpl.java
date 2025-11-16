package com.pulsecare.backend.module.resource.department.service;

import com.pulsecare.backend.module.resource.department.dto.DeptRequestDTO;
import com.pulsecare.backend.module.resource.department.dto.DeptResponseDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Override
    public DeptResponseDTO findById(String id) {
        return null;
    }

    @Override
    public DeptResponseDTO findAll() {
        return null;
    }

    @Override
    public DeptResponseDTO create(DeptRequestDTO data) {
        return null;
    }

    @Override
    public DeptResponseDTO update(DeptRequestDTO data) {
        return null;
    }

    @Override
    public Byte delete(Integer id) {
        return 0;
    }


}
