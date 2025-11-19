package com.pulsecare.backend.common.base.service;

public interface UpdatableService<T, R> {
    R update(T data);
}
