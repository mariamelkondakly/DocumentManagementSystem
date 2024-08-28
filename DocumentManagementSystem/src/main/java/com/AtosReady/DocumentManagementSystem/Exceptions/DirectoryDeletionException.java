package com.AtosReady.DocumentManagementSystem.Exceptions;

import org.springframework.http.HttpStatus;

public class DirectoryDeletionException extends RuntimeException {
    public DirectoryDeletionException(String s) {super((s));}
    public HttpStatus getHttpStatus() {return HttpStatus.EXPECTATION_FAILED;}

}
