package com.pulsecare.backend.common.base.service;

import java.util.List;

public interface FindableService<T, R> {
    R findById(T id);
    List<R> findAll();
}
