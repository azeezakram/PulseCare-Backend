package com.pulsecare.backend.common.base.service;

public interface SavableService<T, R> {
    R save(T data);
}
