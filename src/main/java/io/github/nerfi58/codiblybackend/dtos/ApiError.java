package io.github.nerfi58.codiblybackend.dtos;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(HttpStatus status, LocalDateTime timestamp, String message) {

    public ApiError(HttpStatus status, Throwable exception) {
        this(status, LocalDateTime.now(), exception.getMessage());
    }
}
