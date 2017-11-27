package com.breiler.contribe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NotFoundException extends BaseException {
    private final String message;

    public NotFoundException() {
        this.message = "";
    }

    public NotFoundException(String message) {
        this.message = message;
    }

    public ResponseEntity getResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
