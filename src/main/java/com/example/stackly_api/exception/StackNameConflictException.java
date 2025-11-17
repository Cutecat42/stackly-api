package com.example.stackly_api.exception;

public class StackNameConflictException extends RuntimeException{

    public StackNameConflictException(String message) {
        super(message);
    }
}
