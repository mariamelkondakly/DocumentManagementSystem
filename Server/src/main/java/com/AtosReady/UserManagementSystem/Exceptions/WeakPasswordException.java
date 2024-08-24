package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class WeakPasswordException extends RuntimeException {
    
    public WeakPasswordException(String s) {
        super(s);
    }
    public HttpStatus getStatusCode(){
        return HttpStatus.BAD_REQUEST;
    }
}
