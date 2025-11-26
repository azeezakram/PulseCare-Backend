package com.pulsecare.backend.module.resource.ward.service;

import com.pulsecare.backend.module.resource.ward.model.Ward;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {


    @Override
    public void delete(Integer id) {

    }

    @Override
    public Ward findById(Integer id) {
        return null;
    }

    @Override
    public List<Ward> findAll() {
        return List.of();
    }

    @Override
    public Ward save(Ward data) {
        return null;
    }

    @Override
    public Ward update(Integer integer, Ward data) {
        return null;
    }
}
