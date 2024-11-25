package com.example.codiblybackend.controller;

import com.example.codiblybackend.exception.ApiError;
import com.example.codiblybackend.exception.FailedToGetDataException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleParameterNotValid(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().iterator().next().getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), message);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleParameterMissing(MissingServletRequestParameterException exception) {
        String message = String.format("Required request parameter '%s' is missing.", exception.getParameterName());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), message);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleParameterTypeNotValid(MethodArgumentTypeMismatchException exception) {
        String message = String.format("Parameter '%s' is incorrect type.", exception.getParameter().getParameterName());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), message);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(FailedToGetDataException.class)
    public ResponseEntity<ApiError> handleFailedToGetData(FailedToGetDataException exception) {
        ApiError apiError = new ApiError(HttpStatus.SERVICE_UNAVAILABLE, LocalDateTime.now(), exception.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
