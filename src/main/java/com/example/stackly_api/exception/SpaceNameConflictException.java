package com.example.stackly_api.exception;

public class SpaceNameConflictException extends RuntimeException {

    public SpaceNameConflictException(String message) {
        super(message);
    }
}
