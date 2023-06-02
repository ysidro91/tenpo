package com.ya.tenpo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS, reason = "Exceded request limit per minute")
public class RequestLimitException extends Exception{
}
