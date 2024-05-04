package io.github.nerfi58.codiblybackend.controllers;

import io.github.nerfi58.codiblybackend.dtos.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ApiError> handleParameterNotValid(Exception exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception);
        return new ResponseEntity<>(apiError, apiError.status());
    }
}
