package com.pulsecare.backend.common.base.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UpdatableController<T, R> {
    ResponseEntity<R> update(@Valid T data, BindingResult result);
}
