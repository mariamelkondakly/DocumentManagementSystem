package com.AtosReady.UserManagementSystem.GlobalExceptionHandler;
import com.AtosReady.UserManagementSystem.Exceptions.BadCredentialsException;
import com.AtosReady.UserManagementSystem.Exceptions.UniquenessViolationException;
import com.AtosReady.UserManagementSystem.Exceptions.WeakPasswordException;
import com.AtosReady.UserManagementSystem.Exceptions.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LoggingOnExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<ErrorDetails> shortPasswordHandler(WeakPasswordException ex, WebRequest request){
    ErrorDetails details=new ErrorDetails(ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(details,ex.getStatusCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> userNotFoundHandler(UserNotFoundException ex, WebRequest request){
        ErrorDetails details=new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getHttpStatus());
    }

    @ExceptionHandler(UniquenessViolationException.class)
    public ResponseEntity<ErrorDetails> userAlreadyExistsHandler(UniquenessViolationException ex, WebRequest request){
        ErrorDetails details=new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(details, ex.getHttpStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> wrongPasswordHandler(BadCredentialsException ex, WebRequest request){
        ErrorDetails details= new ErrorDetails(ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(details,ex.getStatusCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this email or national ID already exists.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("A database constraint was violated.");
    }


}
