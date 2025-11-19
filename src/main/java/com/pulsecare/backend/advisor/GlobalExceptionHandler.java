package com.pulsecare.backend.advisor;

import com.pulsecare.backend.common.exception.ValidationException;
import com.pulsecare.backend.common.template.response.ResponseBody;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseBody<Object>> handleValidationError(ValidationException ex) {

        String errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
                new ResponseBody<>(
                        HttpStatus.BAD_REQUEST.value(),
                        errors,
                        null
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody<Object>> handleServerError(Exception ex) {

        return ResponseEntity.internalServerError().body(
                new ResponseBody<>(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error: " + ex.getMessage(),
                        null
                )
        );
    }
}
