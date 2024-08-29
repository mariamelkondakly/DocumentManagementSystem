package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class DirectoryCreationException extends RuntimeException {
    public DirectoryCreationException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
