package com.example.stackly_api.exception;

public class SpaceNotFoundException extends RuntimeException{

    public SpaceNotFoundException(String message) {
        super(message);
    }
}
