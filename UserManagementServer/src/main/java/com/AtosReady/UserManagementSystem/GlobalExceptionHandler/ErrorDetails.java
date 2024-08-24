package com.AtosReady.UserManagementSystem.GlobalExceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class ErrorDetails {
    private String message;
    private String uri;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    public ErrorDetails(){
        timestamp=new Date();
    }
    public ErrorDetails(String message,String uri){
        this();
        this.message=message;
        this.uri=uri;
    }

}
