package com.ya.tenpo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal error")
public class GeneralClientException extends Exception {

    public GeneralClientException(Throwable cause) {
        super(cause);
    }
}
