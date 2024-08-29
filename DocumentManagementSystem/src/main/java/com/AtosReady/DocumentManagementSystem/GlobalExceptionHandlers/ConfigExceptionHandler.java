package com.AtosReady.DocumentManagementSystem.GlobalExceptionHandlers;

import com.AtosReady.DocumentManagementSystem.Exceptions.InvalidSignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

@ControllerAdvice
public class ConfigExceptionHandler {

    @ExceptionHandler(InvalidSignatureException.class)
    public ResponseEntity<ErrorDetails> InvalidJwtHandler(InvalidSignatureException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getStatusCode());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDetails> commandErrorHandler(IOException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.EXPECTATION_FAILED);
    }
}
