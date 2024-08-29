package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class InvalidSignatureException extends RuntimeException {
    public InvalidSignatureException(String message) {
        super(message);
    }

    public HttpStatus getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
