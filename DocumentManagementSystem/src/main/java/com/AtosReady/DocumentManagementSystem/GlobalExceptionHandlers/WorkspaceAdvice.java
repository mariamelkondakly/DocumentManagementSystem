package com.AtosReady.DocumentManagementSystem.GlobalExceptionHandlers;

import com.AtosReady.DocumentManagementSystem.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class WorkspaceAdvice {

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ErrorDetails> WorkspaceExistsHandler(ResourceExistsException ex, WebRequest request){
        ErrorDetails details=new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details,ex.getStatusCode());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> WorkspaceNotFoundHandler(ResourceNotFoundException ex, WebRequest request){
        ErrorDetails details=new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details,ex.getStatusCode());
    }
    @ExceptionHandler(DirectoryCreationException.class)
    public ResponseEntity<String> DirectoryFailedToCreateHandler(DirectoryCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Directory could not be created");
    }

    @ExceptionHandler(DirectoryExistsException.class)
    public ResponseEntity<String> DirectoryAlreadyExistsHandler(DirectoryExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Directory already exists");
    }

    @ExceptionHandler(DirectoryDeletionException.class)
    public ResponseEntity<String> DirectoryDeletionHandler(DirectoryDeletionException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body("Directory couldn't be deleted");
    }
    @ExceptionHandler(DirectoryMoveException.class)
    public ResponseEntity<String> DirectoryMoveHandler(DirectoryMoveException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body("Directory couldn't be moved");
    }
}
