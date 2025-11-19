package com.pulsecare.backend.common.base.service;

public interface DeletableService<T, R> {
    R delete(T id);
}
