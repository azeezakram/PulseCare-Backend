package com.pulsecare.backend.advisor;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.common.template.response.ErrorResponseBody;
import com.pulsecare.backend.module.user.exception.UserInvalidCredentialException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> handleValidationError(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
                new ErrorResponseBody(
                        HttpStatus.BAD_REQUEST.value(),
                        errors
                )
        );
    }

    @ExceptionHandler(UserInvalidCredentialException.class)
    public ResponseEntity<ErrorResponseBody> handleUserInvalidCredentialException(UserInvalidCredentialException ex) {
        return new ResponseEntity<>(
                new ErrorResponseBody(
                        HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage()
                ),
                HttpStatus.UNAUTHORIZED
        );

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponseBody(
                                HttpStatus.NOT_FOUND.value(),
                                ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseBody> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ErrorResponseBody(
                                HttpStatus.CONFLICT.value(),
                                ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleServerError(Exception ex) {

        return ResponseEntity.internalServerError()
                .body(
                        new ErrorResponseBody(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                ex.getMessage()
                        )
                );
    }
}
