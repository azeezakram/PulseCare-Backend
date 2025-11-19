package com.pulsecare.backend.common.base.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface CreatableController<T, R> {
    ResponseEntity<R> create(@Valid T data, BindingResult result);
}
