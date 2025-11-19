package com.pulsecare.backend.common.base.controller;

import org.springframework.http.ResponseEntity;

public interface FindableController<R1, R2, I> {
    ResponseEntity<R1> findById(I id);
    ResponseEntity<R2> findAll();
}
