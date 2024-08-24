package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
    public HttpStatus getStatusCode(){
        return HttpStatus.NOT_FOUND;
    }
}
