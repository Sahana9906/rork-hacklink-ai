package com.hacklink.exception;

import com.hacklink.dto.Dtos;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    ResponseEntity<Dtos.ErrorResponse> handleApi(ApiException exception, HttpServletRequest request) {
        return response(exception.getStatus(), exception.getCode(), exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Dtos.ErrorResponse> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Dtos.ErrorResponse> handleConstraint(ConstraintViolationException exception, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", exception.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<Dtos.ErrorResponse> handleCredentials(BadCredentialsException exception, HttpServletRequest request) {
        return response(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Email or password is incorrect.", request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<Dtos.ErrorResponse> handleUpload(MaxUploadSizeExceededException exception, HttpServletRequest request) {
        return response(HttpStatus.UNPROCESSABLE_ENTITY, "FILE_TOO_LARGE", "The uploaded file is too large.", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Dtos.ErrorResponse> handleIntegrity(DataIntegrityViolationException exception, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "DATA_INTEGRITY_ERROR", "The requested change conflicts with existing data.", request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Dtos.ErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected server error occurred.", request);
    }

    private ResponseEntity<Dtos.ErrorResponse> response(HttpStatus status, String code, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new Dtos.ErrorResponse(code, message, Instant.now(), request.getRequestURI()));
    }
}
