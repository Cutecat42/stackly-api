package com.example.stackly_api.exception;

public class StackNotFoundException extends RuntimeException{

    public StackNotFoundException(String message) {
        super(message);
    }
}
