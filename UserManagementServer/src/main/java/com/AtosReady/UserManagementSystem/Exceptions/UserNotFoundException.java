package com.AtosReady.UserManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus(){
        return HttpStatus.NOT_FOUND;
    }
}
