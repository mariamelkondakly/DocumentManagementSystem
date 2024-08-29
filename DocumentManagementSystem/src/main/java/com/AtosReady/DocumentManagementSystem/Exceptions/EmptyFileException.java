package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
