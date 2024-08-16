package com.AtosReady.DocumentManagementSystem.Exceptions;

public class ShortPasswordException extends RuntimeException {



    public ShortPasswordException() {
        super(String.format("this user cannot be registered due to password being too short"));
    }
}
