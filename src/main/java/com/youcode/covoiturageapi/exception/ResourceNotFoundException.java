package com.youcode.covoiturageapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
