package com.pulsecare.backend.utils;

import com.pulsecare.backend.common.template.response.ResponseBody;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class Validation {
    public static String isRequestDtoValid(BindingResult result) {
            return result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));

    }
}
