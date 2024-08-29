package com.AtosReady.DocumentManagementSystem.GlobalExceptionHandlers;

import com.AtosReady.DocumentManagementSystem.Exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class WorkspaceAdvice {

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ErrorDetails> WorkspaceExistsHandler(ResourceExistsException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getStatusCode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> WorkspaceNotFoundHandler(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getStatusCode());
    }

    @ExceptionHandler(DirectoryCreationException.class)
    public ResponseEntity<ErrorDetails> DirectoryFailedToCreateHandler(DirectoryCreationException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getHttpStatus());
    }

    @ExceptionHandler(DirectoryExistsException.class)
    public ResponseEntity<ErrorDetails> DirectoryAlreadyExistsHandler(DirectoryExistsException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getHttpStatus());
    }

    @ExceptionHandler(DirectoryDeletionException.class)
    public ResponseEntity<ErrorDetails> DirectoryDeletionHandler(DirectoryDeletionException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getHttpStatus());
    }

    @ExceptionHandler(DirectoryMoveException.class)
    public ResponseEntity<ErrorDetails> DirectoryMoveHandler(DirectoryMoveException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getHttpStatus());
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorDetails> EmptyFileHandler(EmptyFileException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getHttpStatus());
    }
}
