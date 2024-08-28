package com.AtosReady.UserManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class DirectoryExistsException extends RuntimeException {
    public DirectoryExistsException(String message) {
        super(message);
    }
    public HttpStatus getHttpStatus(){
        return HttpStatus.CONFLICT;
    }
}
