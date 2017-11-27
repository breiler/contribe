package com.breiler.contribe.exceptions;

import org.springframework.http.ResponseEntity;

public abstract class BaseException extends RuntimeException {
    public abstract ResponseEntity getResponse();
}
