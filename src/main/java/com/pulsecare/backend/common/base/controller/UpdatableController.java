package com.pulsecare.backend.common.base.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UpdatableController<T, R> {
    ResponseEntity<R> update(T data, BindingResult result);
}
