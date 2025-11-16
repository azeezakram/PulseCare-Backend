package com.pulsecare.backend.common.base.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface CreatableController<T, R> {
    ResponseEntity<R> create(T data, BindingResult result);
}
