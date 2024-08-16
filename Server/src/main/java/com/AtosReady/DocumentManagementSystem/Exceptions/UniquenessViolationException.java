package com.AtosReady.DocumentManagementSystem.Exceptions;

import com.AtosReady.DocumentManagementSystem.Models.User;

public class UniquenessViolationException extends RuntimeException {

    public UniquenessViolationException(String typeViolated) {
        super(String.format("This user cannot be registered because this %s already exists.", typeViolated));
    }
}
