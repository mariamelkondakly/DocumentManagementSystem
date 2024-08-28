package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class DirectoryMoveException extends RuntimeException {
    public DirectoryMoveException(String s) {super(s);}
    public HttpStatus getHttpStatus(){
        return HttpStatus.EXPECTATION_FAILED;
    }

}
