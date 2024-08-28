package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String message) {
        super(message);
    }
    public HttpStatus getStatusCode(){
        return HttpStatus.CONFLICT;
    }

}
