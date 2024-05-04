package io.github.nerfi58.codiblybackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class FailedToGetDataException extends RuntimeException {
    
    public FailedToGetDataException(String message) {
        super(message);
    }
}
