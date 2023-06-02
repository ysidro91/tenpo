package com.ya.tenpo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavaliableException extends RuntimeException {

    public ServiceUnavaliableException(String message) {
        super(message);
    }
}
