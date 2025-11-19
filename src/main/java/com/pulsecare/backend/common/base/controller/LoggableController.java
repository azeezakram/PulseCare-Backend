package com.pulsecare.backend.common.base.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface LoggableController<T, R> {
    ResponseEntity<R> login(@Valid T data, BindingResult result);
}
