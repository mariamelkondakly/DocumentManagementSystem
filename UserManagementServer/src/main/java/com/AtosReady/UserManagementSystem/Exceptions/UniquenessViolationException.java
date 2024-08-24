package com.AtosReady.UserManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class UniquenessViolationException extends RuntimeException {

    public UniquenessViolationException(String typeViolated) {
        super(String.format("This user cannot be registered because this %s already exists.", typeViolated));
    }
    public HttpStatus getHttpStatus(){
        return HttpStatus.CONFLICT;
    }
}
