package com.example.codiblybackend.exception;

public class FailedToGetDataException extends RuntimeException {
    public FailedToGetDataException(String message) {
        super(message);
    }
}
